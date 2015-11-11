package monitor.agent_;

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
import test.CustomClassVisitor2;

public class Transformer implements ClassFileTransformer {

	final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
	final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";
	final static List<String> methodList = new ArrayList<String>();

	static {
		methodList.add("java.lang.Throwable.printStackTrace");
	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		// System.out.println("-----------" + className + "--------------");
		if (className.equals("monitor/util/Test1")) {

			Application.setClass(className);
			ClassReader classReader = null;
			try {
				classReader = new ClassReader(className);

				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				CustomClassVisitor2 customClassVisitor = new CustomClassVisitor2(Opcodes.ASM5, classWriter);
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
