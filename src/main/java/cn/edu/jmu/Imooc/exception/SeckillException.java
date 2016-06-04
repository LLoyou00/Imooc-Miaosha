package cn.edu.jmu.Imooc.exception;

/**
 * 秒杀异常
 * @author LLoyou00
 *
 */
public class SeckillException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3092421021489906484L;

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SeckillException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
