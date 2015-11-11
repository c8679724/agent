package test;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CustomClassVisitor extends ClassVisitor implements Opcodes {

	public CustomClassVisitor(int api, ClassVisitor cv) {
		super(api, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

		if ((!(name.equals("<init>") || "<clinit>".equals(name))) && (mv != null)) {

			// 获取方法的参数类型
			// 获取方法的参数名

			// 为方法添加计时功能
			
			mv = new CustomMethodAdviceAdapter(Opcodes.ASM5, mv, access, name, desc);
		}
		return mv;
	}
}
