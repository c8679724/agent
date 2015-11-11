package monitor.config.aop;

public class ConfigAop {

	/**
	 * 想要被监控的类或方法的正则表达式
	 */
	private String[] monitorPatterns;

	public String[] getMonitorPatterns() {
		return monitorPatterns;
	}

	public void setMonitorPatterns(String[] monitorPatterns) {
		this.monitorPatterns = monitorPatterns;
	}

}
