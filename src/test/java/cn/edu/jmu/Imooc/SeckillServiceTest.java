package cn.edu.jmu.Imooc;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cn.edu.jmu.Imooc.dto.Exposer;
import cn.edu.jmu.Imooc.dto.SeckillExecution;
import cn.edu.jmu.Imooc.entity.Seckill;
import cn.edu.jmu.Imooc.service.SeckillService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:spring/spring-dao.xml",
	"classpath:spring/spring-service.xml"
})
public class SeckillServiceTest {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testGetSeckillList() {
		List<Seckill> list = seckillService.getSeckillList();
		logger.info("list={}",list);
	}

	@Test
	public void testGetById() {
		long id = 1000;
		Seckill seckill = seckillService.getById(id);
		logger.info("seckill={}",seckill);
	}

	@Test
	public void testExportSeckillUrl() {
		long id = 1003;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		logger.info("exposer={}",exposer);
	}

	@Test
	public void testExecuteSeckill() {
		long id = 1001;
		long phone = 15606006454l;
		String md5 = "564d717bde2b0d47bf78ea429b4b8535";
		SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
		logger.info("result={}",seckillExecution);
	}
	
	@Test
	public void testExecuteSeckillProcudure() {
		long id = 1003;
		long phone = 156060064564l;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(id, phone, exposer.getMd5());
		logger.info("result={}",seckillExecution);
	}
}
