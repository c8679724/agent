package monitor.agent_;

import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class Transformer2 implements ClassFileTransformer {

	final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
	final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";
	final static List<String> methodList = new ArrayList<String>();

	static {
		methodList.add("java.lang.Throwable.printStackTrace");
	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		System.out.println("-----------" + className + "--------------");

		if (className.equals("monitor/util/Test")) {
			// if (className.equals("java/lang/Throwable")) {
			className = className.replace("/", ".");
			CtClass ctclass = null;
			String methodName = "getNumber";
			// String methodName = "printStackTrace";
			try {
				ctclass = ClassPool.getDefault().get(className);
				ctclass.defrost();
				// 得到这方法实例
				CtClass[] paramTypes = { ClassPool.getDefault().get(PrintStream.class.getName()) };
				// CtMethod ctmethod = ctclass.getDeclaredMethod(methodName,
				// paramTypes);
				CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);

				// 新定义一个方法叫做比如sayHello$impl
				String newMethodName = methodName + "$impl";
				// 原来的方法改个名字
				ctmethod.setName(newMethodName);
				ctmethod.insertBefore("\n monitor.agent.Logger.logger(\"" + newMethodName + "\"); \n");

				// 创建新的方法，复制原来的方法 ，名字为原来的名字
				CtMethod newMethod = CtNewMethod.copy(ctmethod, methodName, ctclass, null);
				// 构建新的方法体
				String outputStr = "\n System.out.println(\"this method " + methodName
						+ " cost:\" +(endTime - startTime) +\"ms.\");";
				StringBuilder bodyStr = new StringBuilder();
				bodyStr.append("{");
				bodyStr.append(prefix);
				// 调用原有代码，类似于method();($$)表示所有的参数
				bodyStr.append("int res= " + newMethodName + "($$);\n");
				// bodyStr.append(newMethodName + "($$);\n");
				bodyStr.append(postfix);
				bodyStr.append(outputStr);
				bodyStr.append("\n");
				bodyStr.append("return res;");
				bodyStr.append("\n}");

				// 替换新方法
				newMethod.setBody(bodyStr.toString());
				// 增加新方法
				ctclass.addMethod(newMethod);
				return ctclass.toBytecode();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//
		// ClassReader cr = new ClassReader(classfileBuffer);
		// ClassNode cn = new ClassNode();
		// cr.accept(cn, 0);
		// for (Object obj : cn.methods) {
		// MethodNode md = (MethodNode) obj;
		// if ("<init>".endsWith(md.name) || "<clinit>".equals(md.name)) {
		// continue;
		// }
		// InsnList insns = md.instructions;
		// InsnList il = new InsnList();
		// il.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System",
		// "out", "Ljava/io/PrintStream;"));
		// il.add(new LdcInsnNode("Enter method-> " + cn.name + "." + md.name));
		// il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
		// "java/io/PrintStream", "println",
		// "(Ljava/lang/String;)V"));
		// insns.insert(il);
		// md.maxStack += 3;
		//
		// }
		// ClassWriter cw = new ClassWriter(0);
		// cn.accept(cw);
		// return cw.toByteArray();

		return classfileBuffer;
	}
}
