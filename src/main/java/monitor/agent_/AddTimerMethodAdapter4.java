package monitor.agent_;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class AddTimerMethodAdapter4 extends LocalVariablesSorter implements Opcodes {

	private int time;
	private String name;

	public AddTimerMethodAdapter4(int access, String desc, MethodVisitor mv, String name) {
		super(Opcodes.ASM5, access, desc, mv);
		this.name = name;
	}

	@Override
	public void visitCode() {

		super.visitCode();

		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("onMethodEnter-> " + name);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

		mv.visitTypeInsn(Opcodes.NEW, "monitor/agent/MonitorModel");
		mv.visitInsn(Opcodes.DUP);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "monitor/agent/MonitorModel", "<init>", "()V", false);
		mv.visitVarInsn(Opcodes.ASTORE, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "monitor/agent/MonitorModel", "before", "()V", false);
	}

	@Override
	public void visitInsn(int opcode) {

		if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {

			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "monitor/agent/MonitorModel", "after", "()V",
					false);
			
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("onMethodExit-> " + name);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			
		}
		super.visitInsn(opcode);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {

		super.visitMaxs(maxStack + 4, maxLocals);
	}
}
