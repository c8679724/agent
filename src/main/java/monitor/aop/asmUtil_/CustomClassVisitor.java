package monitor.aop.asmUtil_;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CustomClassVisitor extends ClassVisitor implements Opcodes {

	private boolean isInterface;

	public CustomClassVisitor(int api, ClassVisitor cv) {

		super(api, cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		cv.visit(version, access, name, signature, superName, interfaces);

		isInterface = (access & ACC_INTERFACE) != 0;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		
		if (!isInterface && (!(name.equals("<init>") || "<clinit>".equals(name))) && (mv != null)) {

			// 获取方法的参数类型
			// 获取方法的参数名

			// 为方法添加计时功能

			mv = new CustomMethodAdviceAdapter(Opcodes.ASM5, mv, access, name, desc);
		}
		return mv;
	}
}
