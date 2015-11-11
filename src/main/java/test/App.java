package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class App extends ClassLoader {

	public void getTest() {
	}

	public static void main(String[] args) throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, SecurityException, InstantiationException, NoSuchMethodException {

		// Test1.class.getMethods()[1].invoke(Test1.class.newInstance(), new
		// Object[] {});

		ClassReader classReader = new ClassReader(Test1.class.getName());
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		CustomClassVisitor customClassVisitor = new CustomClassVisitor(Opcodes.ASM5, classWriter);
		classReader.accept(customClassVisitor, 0);

		byte[] code = classWriter.toByteArray();
		File file = new File("h:/test/asm/Test1.class");
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(file);
			fout.write(code);
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 自定义加载器
		App loader = new App();
		Class<?> test1Class = loader.defineClass(null, code, 0, code.length);
		test1Class.getMethods()[0].invoke(test1Class.newInstance(), new Object[] {});

		// FileOutputStream f=new FileOutputStream(new
		// File("d:"+File.separator+"ok2.class"));
		// f.write(code);;
		// f.close();

	}
}
