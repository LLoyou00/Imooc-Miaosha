package cn.edu.jmu.Imooc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import cn.edu.jmu.Imooc.dao.SeckillDao;
import cn.edu.jmu.Imooc.dao.SuccessKilledDao;
import cn.edu.jmu.Imooc.dao.cache.RedisDao;
import cn.edu.jmu.Imooc.dto.Exposer;
import cn.edu.jmu.Imooc.dto.SeckillExecution;
import cn.edu.jmu.Imooc.entity.Seckill;
import cn.edu.jmu.Imooc.entity.SuccessKilled;
import cn.edu.jmu.Imooc.enums.SeckillStateEnum;
import cn.edu.jmu.Imooc.exception.RepeatKillException;
import cn.edu.jmu.Imooc.exception.SeckillCloseException;
import cn.edu.jmu.Imooc.exception.SeckillException;
import cn.edu.jmu.Imooc.service.SeckillService;

@Service
public class SeckillServiceImpl implements SeckillService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//注入service依赖
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private SuccessKilledDao successKillDao;
	
	@Autowired
	private RedisDao redisDao;
	
	@Override
	public List<Seckill> getSeckillList() {
		// TODO Auto-generated method stub
		
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		// TODO Auto-generated method stub
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		//优化点：缓存优化
		//访问redis
		Seckill seckill;
		
		seckill = redisDao.getSeckill(seckillId);
		if(seckill ==null){
			seckill = seckillDao.queryById(seckillId);
			if(seckill == null){
				return new Exposer(false, seckillId);
			}else{
				redisDao.putSeckill(seckill);
			}
		}
		
		
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		if(nowTime.getTime() < startTime.getTime()
				|| nowTime.getTime() > endTime.getTime()){
			return new Exposer(false, seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
		}
		
		
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}
	
	@Override
	@Transactional //声明式事务控制
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, SeckillCloseException, RepeatKillException {
		if(md5 == null || !md5.equals(getMD5(seckillId))){
			throw new SeckillCloseException("seckill data rewrite");
		}
		
		//执行秒杀逻辑:减库存   记录秒杀行为
		
		Date nowTime = new Date();
		//优化点
		try {
			//记录秒杀行为
			int insertCount = successKillDao.insertSuccessKilled(seckillId, userPhone);
			if(insertCount <= 0){
				//重复秒杀
				throw new RepeatKillException("seckill repeated");
			}else{
				//减库存
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				if(updateCount <= 0){
					//没有更新记录，秒杀结算
					throw new SeckillCloseException("seckill is closed");
				}else{
					//秒杀成功
					SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
				}	
			}
			
			
		}catch(SeckillCloseException e1){
			throw e1;
		}catch(RepeatKillException e2){
			throw e2;
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(),e);
			
			//将编译器异常 转化为运行期异常
			throw new SeckillException("seckill inner error:"+e.getMessage());
		}
		
	}

	/**
	 * 存储过程执行秒杀
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
		if(md5 == null || !md5.equals(getMD5(seckillId))){
			new SeckillExecution(seckillId,SeckillStateEnum.DATA_REWRITE);
		}
		
		Date killTime = new Date();
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		
		try {
			seckillDao.killByProcedure(map);
			Integer result = MapUtils.getInteger(map, "result",-2);
			if(result == 1){
				SuccessKilled sk = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,sk);
			}else{
				return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
		}
		
	}
	
	/**
	 * 获取MD5字符串
	 * @param seckillId
	 * @return
	 */
	private String getMD5(long seckillId){
		//混淆字符串
		String slat = "asd456as4KLAJSA7@!$!454as12321/*";
		
		String base = seckillId + "/" +slat;
		
		return DigestUtils.md5DigestAsHex(base.getBytes());
	}
}
