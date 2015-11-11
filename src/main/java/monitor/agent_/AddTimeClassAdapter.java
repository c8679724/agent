package monitor.agent_;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class AddTimeClassAdapter extends ClassVisitor implements Opcodes {

	private String owner;
	private boolean isInterface;

	public AddTimeClassAdapter(int api) {
		super(api);
	}

	public AddTimeClassAdapter(final int api, final ClassVisitor cv) {
		super(api, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		System.out.println((!(name.equals("<init>") || "<clinit>".equals(name))));
		System.out.println(!isInterface);
		System.out.println(mv != null);

		if ((!(name.equals("<init>") || "<clinit>".equals(name))) && !isInterface && (mv != null)) {
			// 为方法添加计时功能
			// mv = new AddTimeMethodAdviceAdapter(Opcodes.ASM5, mv, access,
			// name, desc);
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("Enter method-> " + name);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		}
		return mv;
	}
}
