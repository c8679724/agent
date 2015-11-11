package monitor.agent_;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * agent 入口
 *
 * 这里提供了agent入口，获取到jvm中加载的所有类，可以通过asm或者javassist对已经加载的类添加我们想要的代码
 *
 * @author sky
 * @date 2015年10月13日
 */
public class Agent {

	private static Instrumentation instrumentation;

	// 通过代码加载agent
	public static void agentmain(String args, Instrumentation inst) {
		System.out.println("agentmain");
		agent(args, inst);
	}

	// 通过jvm参数加载agent
	public static void premain(String args, Instrumentation inst) {
		System.out.println("premain");
		agent(args, inst);
	}

	final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
	final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";

	@SuppressWarnings("rawtypes")
	public static void agent(String args, Instrumentation inst) {

		// Class[] classes = inst.getAllLoadedClasses();
		// for (Class cls : classes) {
		// System.out.println(cls.getName());
		// }
		System.out.println("stating agent.........");

		inst.addTransformer(new Transformer(), true);

		// inst.addTransformer(new TransformerThrowable(), true);
		Class test = null;
		Class test1 = null;
		try {
			test = Class.forName("monitor.util.Test");
			test1 = Class.forName("monitor.util.Test1");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			inst.retransformClasses(test,test1);
		} catch (UnmodifiableClassException e) {
			e.printStackTrace();
		}

		// try {
		// inst.redefineClasses(
		// new ClassDefinition(java.lang.Throwable.class,
		// ThrowableAspectUtil.getThrowableClassBytes3()));
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// } catch (UnmodifiableClassException e) {
		// e.printStackTrace();
		// }

		// try {
		// Class.forName("java.lang.Throwable");
		// } catch (ClassNotFoundException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		// try {
		// throw new Throwable("1--1");
		// } catch (Throwable e) {
		// e.printStackTrace();
		// }

		// try {
		// inst.redefineClasses(
		// new ClassDefinition(java.lang.Throwable.class,
		// ThrowableAspectUtil.getThrowableClassBytes()));
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// } catch (UnmodifiableClassException e) {
		// e.printStackTrace();
		// }
	}

	public long getObjectSize(Object objectToSize) {
		return instrumentation.getObjectSize(objectToSize);
	}

	public void addClassTransformer(ClassFileTransformer transformer, boolean canRetransform) {
		instrumentation.addTransformer(transformer, canRetransform);
	}
}
