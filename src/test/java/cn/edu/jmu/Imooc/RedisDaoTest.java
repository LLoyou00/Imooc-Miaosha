package cn.edu.jmu.Imooc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cn.edu.jmu.Imooc.dao.SeckillDao;
import cn.edu.jmu.Imooc.dao.cache.RedisDao;
import cn.edu.jmu.Imooc.entity.Seckill;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:spring/spring-dao.xml",
})
public class RedisDaoTest {
	
	private long Id = 1001;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSeckill() {
		Seckill seckill = redisDao.getSeckill(Id);
		if(seckill == null){
			seckill = seckillDao.queryById(Id);
			if(seckill != null){
				String result = redisDao.putSeckill(seckill);
				System.out.println(result);
				seckill = redisDao.getSeckill(Id);
				System.out.println(seckill);
			}
		}
	}

}
