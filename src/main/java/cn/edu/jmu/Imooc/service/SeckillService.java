package cn.edu.jmu.Imooc.service;

import java.util.List;
import cn.edu.jmu.Imooc.dto.Exposer;
import cn.edu.jmu.Imooc.dto.SeckillExecution;
import cn.edu.jmu.Imooc.entity.Seckill;
import cn.edu.jmu.Imooc.exception.RepeatKillException;
import cn.edu.jmu.Imooc.exception.SeckillCloseException;
import cn.edu.jmu.Imooc.exception.SeckillException;

/**
 * 
 * @author LLoyou00
 *
 */
public interface SeckillService {
	
	/**
	 * 查询所有秒杀记录
	 * @return
	 */
	List<Seckill> getSeckillList();
	
	
	/**
	 * 查询单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	
	
	/**
	 * 秒杀开启时输出接口地址，
	 * 否则输出系统时间和秒杀时间
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	
	/**
	 * 执行秒杀操作
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5)
		throws SeckillException,SeckillCloseException,RepeatKillException;
	
	/**
	 * 通过存储过程完成秒杀
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
	SeckillExecution executeSeckillProcedure(long seckillId,long userPhone,String md5);
		
}
