package monitor;

import monitor.aop.MethodNode;

public class Application {

	public static final ThreadLocal<String> packageName = new ThreadLocal<String>();
	public static final ThreadLocal<String> className = new ThreadLocal<String>();

	public static final ThreadLocal<Long> fathterMethodRunIndex = new ThreadLocal<Long>();
	public static final ThreadLocal<Long> methodRunIndex = new ThreadLocal<Long>();
	public static final ThreadLocal<MethodNode> methodNodeTree = new ThreadLocal<MethodNode>();

	/**
	 * 设置正在处理的类的包名、类名到当前线程中，以便硬编码这些信息到切面中
	 * 
	 * @param clazz
	 */
	public static void setClass(Class<?> clazz) {
		Application.packageName.set(clazz.getPackage().getName());
		Application.className.set(clazz.getSimpleName());
	}

	/**
	 * 设置正在处理的类的包名、类名到当前线程中，以便硬编码这些信息到切面中
	 * 
	 * @param className_
	 */
	public static void setClass(String className_) {
		className_ = className_.replaceAll("/", ".");
		String s[] = className_.split("/");
		String className = s[s.length - 1];
		String packageName = className_.substring(0, className_.length() - className.length());
		Application.packageName.set(packageName);
		Application.className.set(className);
	}
	

	/**
	 * 设置正在处理的类的包名、类名到当前线程中，以便硬编码这些信息到切面中
	 * 
	 * @param className_
	 */
	public static void setClass(String packageName,String className_) {
		Application.packageName.set(packageName);
		Application.className.set(className_);
	}


	public static final long getMethodRunIndex(long fatherRunIndex) {

		if (fatherRunIndex == 0L) {

		} else {

		}

		long index = Application.methodRunIndex.get() == null ? 0 : Application.methodRunIndex.get() + 1L;
		Application.methodRunIndex.set(index);
		return index;
	}
}
