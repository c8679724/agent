package monitor.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import monitor.Application;
import monitor.aop.ClassesChoose;
import monitor.aop.asmUtil.CustomClassVisitor;

public class Transformer implements ClassFileTransformer {

	final static List<Class<?>> transform_after_Classese = new ArrayList<Class<?>>();

	public static boolean isTransform(Class<?> clazz) {
		for (Class<?> clazz1 : transform_after_Classese) {
			if (clazz1.equals(clazz)) {
				return true;
			}
		}
		return false;
	}

	public static void addTransform_after_Classese(Class<?> clazz) {
		transform_after_Classese.add(clazz);
	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		boolean c = false;
		boolean isTransform = false;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className.replace('/', '.'));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		// 判断是否已经转换过了
		isTransform = Transformer.isTransform(clazz);
		if (isTransform) {
			return classfileBuffer;
		}

		Transformer.addTransform_after_Classese(clazz);
		c = ClassesChoose.needTransformer(clazz);
		if (c) {
			// 设置正在处理的类的包名、类名到当前线程中，以便硬编码这些信息到切面中
			Application.setClass(clazz);

			// 开始转换类的字节码
			ClassReader classReader = null;
			try {
				classReader = new ClassReader(className);
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				CustomClassVisitor customClassVisitor = new CustomClassVisitor(Opcodes.ASM5, classWriter);
				classReader.accept(customClassVisitor, 0);
				return classWriter.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
				return classfileBuffer;
			}
		} else {
			return classfileBuffer;
		}
	}
}
