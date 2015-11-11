package monitor.agent_;

/**
 * AOP切面
 * 
 * @author sky
 *
 */
public class Aspect {

	private static ThreadLocal<Long> statTime = new ThreadLocal<Long>();

	/**
	 * 执行目标方法前
	 */
	public static void before() {
		// 方法开始时间
		statTime.set(System.currentTimeMillis());
		System.out.println("before:");
	}

	/**
	 * 执行目标方法后
	 */
	public static void after() {
		System.out.print("after:");
		System.out.println(System.currentTimeMillis() - statTime.get());
	}
}
