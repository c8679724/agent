package monitor.agent_;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class AddTimeMethodAdviceAdapter extends AdviceAdapter {

	private String owner;

	private int timeType;

	private boolean isInterface;

	private String name;

	protected AddTimeMethodAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
		
		super(api, mv, access, name, desc);
		this.name = name;
	}

	@Override
	protected void onMethodEnter() {
//		super.visitCode();
		// mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
		//
		// "currentTimeMillis", "()J", false);
		// timeType = newLocal(Type.LONG_TYPE);
		// mv.visitVarInsn(LSTORE, timeType);

		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("onMethodEnter-> " + name);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

	}

	@Override
	protected void onMethodExit(int opcode) {

		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("onMethodExit-> " + name);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

		// mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
		// "currentTimeMillis", "()J", false);
		// mv.visitVarInsn(LLOAD, timeType);
		// mv.visitInsn(LSUB);
		// mv.visitVarInsn(LSTORE, timeType);
		//
		// mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
		// "Ljava/io/PrintStream");
		// mv.visitVarInsn(LLOAD, timeType);
		// mv.visitMethodInsn(INVOKESTATIC, "java/io/PrintStream", "println",
		// "(Ljava/lang/String;)V", false);

		// mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
		// mv.visitInsn(LADD);
		// mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");

		super.visitInsn(opcode);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {

		super.visitMaxs(maxStack + 4, maxLocals);

	}
}
