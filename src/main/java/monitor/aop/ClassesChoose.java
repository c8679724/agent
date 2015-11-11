package monitor.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassesChoose {

	private static Pattern[] patterns;

	private static Pattern[] jdkPatterns;
	
	/**
	 * 反向正则
	 */
	static Pattern[] exPatterns;

	/**
	 * 
	 * @param classes
	 * @return
	 */
	public static Class<?>[] chooseClasses(Class<?>[] classes) {
		List<Class<?>> classes2 = new ArrayList<Class<?>>();
		boolean r = false;
		Class<?> classIndex = null;
		jdkPatterns = new Pattern[6];
		Matcher matcher = null;
		boolean mr = false;
		jdkPatterns[0] = Pattern.compile("javax.*");
		jdkPatterns[1] = Pattern.compile("com.sun.*");
		jdkPatterns[2] = Pattern.compile("sun..*");
		jdkPatterns[3] = Pattern.compile("java.*");
		jdkPatterns[4] = Pattern.compile("sunw.*");
		jdkPatterns[5] = Pattern.compile("\\[.*");
		for (int i = 0; i < classes.length; i++) {
			classIndex = classes[i];
			String clazzPath = classIndex.getName();
			// 这个类如果是jdk自带的类
			for (int j = 0; j < jdkPatterns.length; j++) {
				matcher = jdkPatterns[j].matcher(clazzPath);
				mr = matcher.matches();
				if (mr) {
					break;
				}
			}
			if (!mr) {
				// 匹配方法规则
				r = needTransformer(classIndex);
				if (r) {
					classes2.add(classIndex);
				}
			}
		}
		Class<?>[] rClassese = new Class<?>[classes2.size()];
		return classes2.toArray(rClassese);
	}

	/**
	 * 匹配class里面的每一个方法是否有符合规则的，只要有一个的话就需要做转换(返回true)
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean needTransformer(Class<?> clazz) {

		String clazzPath = clazz.getPackage().getName() + "." + clazz.getSimpleName() + ".";
		Method[] methods = clazz.getMethods();
		String methodPath = "";
		for (int i = 0; i < methods.length; i++) {
			Class<?>[] parameterTypes = methods[i].getParameterTypes();
			String argsType = "";
			for (int j = 0; j < parameterTypes.length; j++) {
				argsType += parameterTypes[j].getName();
			}
			methodPath = clazzPath + methods[i].getName() + "(" + argsType + ")";

			// 监控内置的规则

			// 用户想要被监控的类和方法
			boolean mr = true;
			Matcher matcher = null;
			for (int j = 0; j < patterns.length; j++) {
				matcher = patterns[j].matcher(methodPath);
				mr = matcher.matches();
				if (mr) {
					return true;
				}
			}

			// 用户的aop切面

		}
		return false;
	}

	public static void setPatterns(Pattern[] patterns) {
		ClassesChoose.patterns = patterns;
	}

}
