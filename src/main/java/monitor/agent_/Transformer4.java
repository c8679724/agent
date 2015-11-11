package monitor.agent_;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class Transformer4 implements ClassFileTransformer {

	final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
	final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";
	final static List<String> methodList = new ArrayList<String>();

	static {
		methodList.add("java.lang.Throwable.printStackTrace");
	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		System.out.println("-----------" + className + "--------------");

		if (className.equals("monitor/util/Test1") || className.equals("monitor/util/Test")) {

			ClassReader cr = new ClassReader(classfileBuffer);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			try {

				CustomVisitor myv = new CustomVisitor(Opcodes.ASM5, cw);
				cr.accept(myv, 0);
				byte[] data = cw.toByteArray();
				File file = new File("h:/test/asm/" + className.split("/")[2] + ".class");
				FileOutputStream fout;
				fout = new FileOutputStream(file);
				fout.write(data);
				fout.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return cw.toByteArray();

		} else {

			return classfileBuffer;
		}
	}
}
