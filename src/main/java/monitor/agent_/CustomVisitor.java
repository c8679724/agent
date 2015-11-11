package monitor.agent_;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CustomVisitor extends ClassVisitor implements Opcodes {

	public CustomVisitor(int api, ClassVisitor cv) {
		super(api, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

		if ((!(name.equals("<init>") || "<clinit>".equals(name))) && (mv != null)) {

			// 为方法添加计时功能
			mv = new AddTimerMethodAdapter4(Opcodes.ASM5, desc, mv, name);
		}
		return mv;
	}
}
