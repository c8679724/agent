package monitor.aop.asmUtil_;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.ParameterNode;

import monitor.agent_.MonitorModel;

public class CustomMethodAdviceAdapter extends AdviceAdapter implements Opcodes {

	private int varLen;

	private String name;

	/**
	 * The method parameter info (access flags and name)
	 */
	public List<ParameterNode> parameters;

	public CustomMethodAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc) {

		super(api, mv, access, name, desc);
		this.name = name;
		Type[] args = Type.getArgumentTypes(desc);

		// System.out.println(args);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {

		// if (start.getOffset() == 0) {
		// System.out.println("localvar:" + name);
		// }
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}

	@Override
	protected void onMethodEnter() {
		// super.visitCode();

		if (!(name.equals("<init>") || "<clinit>".equals(name))) {

			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("onMethodEnterAdviceAdapter-> " + name);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

			mv.visitTypeInsn(Opcodes.NEW, "monitor/agent/MonitorModel");
			mv.visitInsn(Opcodes.DUP);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "monitor/agent/MonitorModel", "<init>", "()V", false);
			varLen = newLocal(Type.getType(MonitorModel.class));
			mv.visitVarInsn(Opcodes.ASTORE, varLen);
		}
	}

	@Override
	protected void onMethodExit(int opcode) {

		if (!(name.equals("<init>") || "<clinit>".equals(name))) {
			if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {

				int rIndex = varLen + 1;
				if (opcode == Opcodes.IRETURN) {
					mv.visitVarInsn(Opcodes.ISTORE, rIndex);
				} else if (opcode == Opcodes.LRETURN) {
					mv.visitVarInsn(Opcodes.LSTORE, rIndex);
				} else if (opcode == Opcodes.FRETURN) {
					mv.visitVarInsn(Opcodes.FSTORE, rIndex);
				} else if (opcode == Opcodes.DRETURN) {
					mv.visitVarInsn(Opcodes.DSTORE, rIndex);
				} else if (opcode == Opcodes.ARETURN) {
					mv.visitVarInsn(Opcodes.ASTORE, rIndex);
				}

				mv.visitVarInsn(Opcodes.ALOAD, varLen);
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "monitor/agent/MonitorModel", "after", "()V", false);

				mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
				mv.visitLdcInsn("onMethodExitAdviceAdapter-> " + name);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

				// NEW java/lang/StringBuilder
				// DUP
				// LDC "t"
				// INVOKESPECIAL java/lang/StringBuilder.<init>
				// (Ljava/lang/String;)V
				// ILOAD 1
				// INVOKEVIRTUAL java/lang/StringBuilder.append
				// (I)Ljava/lang/StringBuilder;
				// INVOKEVIRTUAL java/lang/StringBuilder.toString
				// ()Ljava/lang/String;
				// INVOKEVIRTUAL java/io/PrintStream.println
				// (Ljava/lang/String;)V
				//

				if (opcode != Opcodes.RETURN) {

					mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
					mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
					mv.visitInsn(Opcodes.DUP);
					mv.visitLdcInsn("return:");
					mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>",
							"(Ljava/lang/String;)V", false);

					// append
					if (opcode == Opcodes.IRETURN) {
						mv.visitVarInsn(Opcodes.ILOAD, rIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(I)Ljava/lang/StringBuilder;", false);
					} else if (opcode == Opcodes.LRETURN) {
						mv.visitVarInsn(Opcodes.LLOAD, rIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(J)Ljava/lang/StringBuilder;", false);
					} else if (opcode == Opcodes.FRETURN) {
						mv.visitVarInsn(Opcodes.FLOAD, rIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(F)Ljava/lang/StringBuilder;", false);
					} else if (opcode == Opcodes.DRETURN) {
						mv.visitVarInsn(Opcodes.DLOAD, rIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(D)Ljava/lang/StringBuilder;", false);
					} else if (opcode == Opcodes.ARETURN) {
						mv.visitVarInsn(Opcodes.ALOAD, rIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
					}

					mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;",
							false);
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

					// 把返回值重新写入栈顶
					if (opcode == Opcodes.IRETURN) {
						mv.visitVarInsn(Opcodes.ILOAD, rIndex);
					} else if (opcode == Opcodes.LRETURN) {
						mv.visitVarInsn(Opcodes.LLOAD, rIndex);
					} else if (opcode == Opcodes.FRETURN) {
						mv.visitVarInsn(Opcodes.FLOAD, rIndex);
					} else if (opcode == Opcodes.DRETURN) {
						mv.visitVarInsn(Opcodes.DLOAD, rIndex);
					} else if (opcode == Opcodes.ARETURN) {
						mv.visitVarInsn(Opcodes.ALOAD, rIndex);
					}
				}
			}
		}

		/*
		 * if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode
		 * == Opcodes.ATHROW) { mv.visitFieldInsn(GETSTATIC, "java/lang/System",
		 * "out", "Ljava/io/PrintStream;"); mv.visitLdcInsn(
		 * "onMethodExitAdviceAdapter-> " + name);
		 * mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
		 * "(Ljava/lang/String;)V", false); }
		 */

		//
		// mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
		// "Ljava/io/PrintStream");
		// mv.visitVarInsn(LLOAD, timeIndex);
		// mv.visitMethodInsn(INVOKESTATIC, "java/io/PrintStream", "println",
		// "(Ljava/lang/String;)V", false);

		// mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
		// mv.visitInsn(LADD);
		// mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");

		// super.visitInsn(opcode);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {

		super.visitMaxs(maxStack + 4, maxLocals);

	}
}
