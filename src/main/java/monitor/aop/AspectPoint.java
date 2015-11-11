package monitor.aop;

public abstract class AspectPoint {

	/**
	 * 通过正则表达式设置哪些方法需要安装你的切面
	 * 
	 * @return
	 */
	public abstract String[] setPatterns();

	/**
	 * 方法运行前，会通过字节码重写方式把这个方法插入目标方法的第一行
	 * 
	 * @param joinPoin
	 */
	public abstract void before(JoinPoin joinPoin);

	/**
	 * 方法运行后，返回参数前。会通过字节码重写方式把这个方法插入目标方法的最后一行，仅保留最后的return
	 * 
	 * @param joinPoin
	 */
	public abstract void after(JoinPoin joinPoin);
}
