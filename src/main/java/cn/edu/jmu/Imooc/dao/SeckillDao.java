package cn.edu.jmu.Imooc.dao;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import cn.edu.jmu.Imooc.entity.Seckill;

public interface SeckillDao {
	/**
	 * 减库存
	 * @param seckillId
	 * @param killTime
	 * @return 返回影响的行数，返回 0 表示语句执行失败
	 */
	int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime);
	
	
	/**
	 * 根据id查询秒杀商品
	 * @param secklillId
	 * @return 
	 */
	Seckill queryById(long seckillId);
	
	
	/**
	 * 根据偏移量查询秒杀商品列表
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);
	
	/**
	 * 调用秒杀存储过程
	 * @param paramMap
	 */
	void killByProcedure(Map<String, Object> paramMap);
}
