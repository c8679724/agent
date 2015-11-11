package monitor.aop;

import java.util.List;

public class MethodNode {

	/**
	 * 执行顺序
	 */
	private long run_index;

	/**
	 * 方法名称<http、sql>
	 */
	private String methodName;

	/**
	 * 执行类型：method、http(server、client)、sql、webservice(server、client)、redis、
	 * memcache、 MongoDB
	 */
	private int type;

	/**
	 * 运行时间
	 */
	private long time;

	/**
	 * 子节点
	 */
	private List<MethodNode> methodNodeChildren;

	public long getRun_index() {
		return run_index;
	}

	public void setRun_index(long run_index) {
		this.run_index = run_index;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<MethodNode> getMethodNodeChildren() {
		return methodNodeChildren;
	}

	public void setMethodNodeChildren(List<MethodNode> methodNodeChildren) {
		this.methodNodeChildren = methodNodeChildren;
	}
}
