package monitor.aop;

import org.objectweb.asm.Type;

public class JoinPoin {

	private String[] methodParamsName;
	private String packageName;
	private String className;
	private String methodName;
	private Type[] methodParamsType;
	private Object[] paramsValue;
	private Type returnType;
	private Object returnValue;

	private long fathterMethodRunIndex;
	private long run_index;

	public String[] getMethodParamsName() {
		return methodParamsName;
	}

	public void setMethodParamsName(String[] methodParamsName) {
		this.methodParamsName = methodParamsName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Type[] getMethodParamsType() {
		return methodParamsType;
	}

	public void setMethodParamsType(Type[] methodParamsType) {
		this.methodParamsType = methodParamsType;
	}

	public Object[] getParamsValue() {
		return paramsValue;
	}

	public void setParamsValue(Object[] paramsValue) {
		this.paramsValue = paramsValue;
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public long getFathterMethodRunIndex() {
		return fathterMethodRunIndex;
	}

	public void setFathterMethodRunIndex(long fathterMethodRunIndex) {
		this.fathterMethodRunIndex = fathterMethodRunIndex;
	}

	public long getRun_index() {
		return run_index;
	}

	public void setRun_index(long run_index) {
		this.run_index = run_index;
	}
}
