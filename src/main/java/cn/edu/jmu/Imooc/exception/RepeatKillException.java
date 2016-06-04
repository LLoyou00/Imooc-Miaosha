package cn.edu.jmu.Imooc.exception;


/**
 * 重复秒杀异常(runtime)
 * @author LLoyou00
 *
 */
public class RepeatKillException extends SeckillException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6168312604047626071L;

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RepeatKillException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
