package monitor.support;

import java.math.BigDecimal;

import monitor.aop.AspectPoint;
import monitor.aop.JoinPoin;

/**
 * 方法的时间统计支持
 * 
 * @author sky
 *
 */
public class MethodAnalyze extends AspectPoint {

	/**
	 * 方法执行时间
	 */
	private long time;

	@Override
	public String[] setPatterns() {
		return null;
	}

	@Override
	public void before(JoinPoin joinPoin) {
		time = System.nanoTime();
	}

	@Override
	public void after(JoinPoin joinPoin) {
		time = System.nanoTime() - time;
		System.out.println(joinPoin.getMethodName() + "方法运行时间：" + divide(time, 1000000) + "ms");
	}

	/**
	 * 精确除法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double divide(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, 3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
