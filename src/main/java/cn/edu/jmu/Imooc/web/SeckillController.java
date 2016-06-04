package cn.edu.jmu.Imooc.web;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.edu.jmu.Imooc.dto.Exposer;
import cn.edu.jmu.Imooc.dto.SeckillExecution;
import cn.edu.jmu.Imooc.dto.SeckillResult;
import cn.edu.jmu.Imooc.entity.Seckill;
import cn.edu.jmu.Imooc.enums.SeckillStateEnum;
import cn.edu.jmu.Imooc.exception.RepeatKillException;
import cn.edu.jmu.Imooc.exception.SeckillCloseException;
import cn.edu.jmu.Imooc.service.SeckillService;

/**
 * 
 * @author LLoyou00
 *
 */

@Controller
@RequestMapping("/seckill") //模块名
public class SeckillController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public String list(Model model){
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list",list);
		return "list";
	}
	
	@RequestMapping(value = "/{seckillId}/detail" , method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId,Model model) {
		if(seckillId == null){
			return "redirect:/seckill/list";
		}
		
		Seckill seckill = seckillService.getById(seckillId);
		if(seckill == null){
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	
	//AJAX接口
	@RequestMapping(value = "/{seckillId}/exposer",
					method = RequestMethod.POST,
					produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
		SeckillResult<Exposer> result;
		
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(),e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
	
		return result;
	}
	
	
	@RequestMapping(value = "/{seckillId}/{md5}/execution",
					method = RequestMethod.POST,
					produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
													@PathVariable("md5") String md5,
													@CookieValue(value = "killPhone",required = false) Long phone) {
		if(phone == null){
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}

		try {
			SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
			return new SeckillResult<SeckillExecution>(true, execution);
		}catch(RepeatKillException e){
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEATE_KILL);
			return new SeckillResult<SeckillExecution>(true, execution);
		}catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
			return new SeckillResult<SeckillExecution>(true, execution);
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(),e);
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(true, execution);
		}
	}
	
	@RequestMapping(value = "/time/now" ,method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time() {
		Date now = new Date();
		return new SeckillResult<Long>(true,now.getTime());
	}
}
