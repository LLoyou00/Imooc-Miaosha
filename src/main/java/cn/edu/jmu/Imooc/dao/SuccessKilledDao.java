package cn.edu.jmu.Imooc.dao;

import org.apache.ibatis.annotations.Param;
import cn.edu.jmu.Imooc.entity.SuccessKilled;


public interface SuccessKilledDao {

	/**
	 * 插入秒杀成功明细,可过滤重复秒杀
	 * @param seckillId
	 * @param userPhone
	 * @return 表示插入的行数，返回 0 表示插入失败
	 */
	int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);
	
	
	/**
	 * 根据id查询SuccessKilled并携带秒杀产品本身
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);
	
}
