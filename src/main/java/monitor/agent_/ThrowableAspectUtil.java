package monitor.agent_;

import java.io.IOException;
import java.io.PrintStream;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class ThrowableAspectUtil {

	final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
	final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";

	public static byte[] getExceptionClassBytes() {

		String className = "java.lang.Exception";
		CtClass ctclass = null;

		try {
			ctclass = ClassPool.getDefault().get(className);
			return ctclass.toBytecode();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] getThrowableClassBytes() {
		String className = "java.lang.Throwable";
		CtClass ctclass = null;
		// String methodName = "getNumber";
		String methodName = "printStackTrace";
		try {
			ctclass = ClassPool.getDefault().get(className);
			ctclass.defrost();
			// 得到这方法实例
			CtClass[] paramTypes = { ClassPool.getDefault().get(PrintStream.class.getName()) };
			CtMethod ctmethod = ctclass.getDeclaredMethod(methodName, paramTypes);
			// CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);

			// 新定义一个方法叫做比如sayHello$impl
			String newMethodName = methodName + "$impl";
			// 原来的方法改个名字
			ctmethod.setName(newMethodName);
			// 创建新的方法，复制原来的方法 ，名字为原来的名字
			CtMethod newMethod = CtNewMethod.copy(ctmethod, methodName, ctclass, null);
			// 构建新的方法体
			String outputStr = "\n System.out.println(\"this method " + methodName
					+ " cost:\" +(endTime - startTime) +\"ms.\");";
			StringBuilder bodyStr = new StringBuilder();
			bodyStr.append("{");
			bodyStr.append(prefix);
			// 调用原有代码，类似于method();($$)表示所有的参数
			// bodyStr.append("int res= " + newMethodName + "($$);\n");
			bodyStr.append(newMethodName + "($$);\n");
			bodyStr.append(postfix);
			bodyStr.append(outputStr);
			bodyStr.append("\n");
			// bodyStr.append("return res;");
			bodyStr.append("\n}");

			// 替换新方法
			newMethod.setBody(bodyStr.toString());
			// 增加新方法
			ctclass.addMethod(newMethod);
			return ctclass.toBytecode();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				ClassPool.getDefault().get(className).toBytecode();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (CannotCompileException e1) {
				e1.printStackTrace();
			} catch (NotFoundException e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

	public static byte[] getThrowableClassBytes1() {
		String className = "java.lang.Throwable";
		String methodName = "printStackTrace";
		String outputStr = "\nSystem.out.println(\"this method " + methodName
				+ " cost:\" +(endTime - startTime) +\"ms.\");";
		CtClass ctclass = null;
		// 用于取得字节码类，必须在当前的classpath中，使用全称 ,这部分是关于javassist的知识
		try {
			ctclass = ClassPool.getDefault().get(className);

			// 得到这方法实例
			CtClass[] paramTypes = { ClassPool.getDefault().get(PrintStream.class.getName()) };
			CtMethod ctmethod = ctclass.getDeclaredMethod(methodName, paramTypes);

			ctmethod.insertBefore("\n System.out.println(\"test1 printStackTrace\"); \n  System.out.println(\"\"); \n");
			ctmethod.insertBefore("\n System.out.println(\" test--:\"+ this.getMessage()); \n");
			ctmethod.insertBefore("\n System.out.println(\"\"); \n");
			//
			//
			// ctmethod.insertBefore("\n long startTime =
			// System.currentTimeMillis(); \n");
			// ctmethod.insertBefore("\n long startTime1 =
			// System.currentTimeMillis(); \n");

			ctclass.rebuildClassFile();
			// 调用原有代码，类似于method();($$)表示所有的参数

			ctmethod.insertAfter("\n System.out.println(\"test2 printStackTrace \");\n");
			Object o = ctmethod.getMethodInfo().getAttributes();
			// ctmethod.insertAfter("\n System.out.println(startTime1);\n");

			return ctclass.toBytecode();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] getThrowableClassBytes2() {
		String className = "java.lang.Throwable";
		String methodName = "printStackTrace";
		String outputStr = "\nSystem.out.println(\"this method " + methodName
				+ " cost:\" +(endTime - startTime) +\"ms.\");";
		CtClass ctclass = null;
		// 用于取得字节码类，必须在当前的classpath中，使用全称 ,这部分是关于javassist的知识
		try {
			ctclass = ClassPool.getDefault().get(className);

			
			CtMethod[]  ms =	ctclass.getMethods();
			for (int i = 0; i < ms.length; i++) {
				System.out.println(ms[i].getLongName());
			}
			
			// 得到这方法实例
			CtClass[] paramTypes = { ClassPool.getDefault().get(PrintStream.class.getName()) };
			CtMethod ctmethod = ctclass.getDeclaredMethod(methodName, paramTypes);

			ctmethod.insertBefore("\n System.out.println(\"test1 printStackTrace\"); \n  System.out.println(\"\"); \n");
			ctmethod.insertBefore("\n System.out.println(\" test--:\"+ this.getMessage()); \n");
			ctmethod.insertBefore("\n System.out.println(\"\"); \n");
			//
			//
			// ctmethod.insertBefore("\n long startTime =
			// System.currentTimeMillis(); \n");
			// ctmethod.insertBefore("\n long startTime1 =
			// System.currentTimeMillis(); \n");

			ctclass.rebuildClassFile();
			// 调用原有代码，类似于method();($$)表示所有的参数

			ctmethod.insertAfter("\n System.out.println(\"test2 printStackTrace \");\n");
			Object o = ctmethod.getMethodInfo().getAttributes();
			// ctmethod.insertAfter("\n System.out.println(startTime1);\n");

			return ctclass.toBytecode();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] getThrowableClassBytes3() {

		String className = "java.lang.Throwable";
		CtClass ctclass = null;

		try {
			ctclass = ClassPool.getDefault().get(className);
			return ctclass.toBytecode();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
