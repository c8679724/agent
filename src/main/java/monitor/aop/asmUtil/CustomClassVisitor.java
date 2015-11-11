package monitor.aop.asmUtil;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CustomClassVisitor extends ClassVisitor implements Opcodes {

	/**
	 * 是否是接口类
	 */
	private boolean isInterface;

	public CustomClassVisitor(int api, ClassVisitor cv) {
		super(api, cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		isInterface = (access & ACC_INTERFACE) != 0;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

		/**
		 * 是否是抽象方法
		 */
		boolean is_abstract;
		// 判断是否是抽象方法
		if ((access & Opcodes.ACC_ABSTRACT) == 0) {
			is_abstract = false;
		} else {
			is_abstract = true;
		}

		// 判断这个方法是否达到添加切面链的条件
		// .
		// .
		// .
		// .

		if (!is_abstract && !isInterface && mv != null && (!(name.equals("<init>") || "<clinit>".equals(name)))) {

			try {
				mv = new CustomMethodAdviceAdapter(Opcodes.ASM5, mv, access, name, desc);
			} catch (Exception e) {
			}
			
		}
		return mv;
	}
}
