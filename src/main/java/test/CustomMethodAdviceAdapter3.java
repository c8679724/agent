package test;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import monitor.Application;
import monitor.aop.Point;

public class CustomMethodAdviceAdapter3 extends AdviceAdapter {

	private int timeLocalVariableIndex;

	private int varLocalVariableIndex1;
	private int varLocalVariableIndex2;

	private final String packageName;

	private final String className;

	private String methodName;

	private final String methodDescriptor;

	public CustomMethodAdviceAdapter3(int api, MethodVisitor mv, int access, String name, String desc) {

		super(api, mv, access, name, desc);
		this.methodName = name;
		this.methodDescriptor = desc;
		this.packageName = Application.packageName.get();
		this.className = Application.className.get();
	}

	@Override
	protected void onMethodEnter() {
		// super.visitCode();

		if (!(methodName.equals("<init>") || "<clinit>".equals(methodName))) {

			// Point point = new Point("","", "", "");

			// Object[] paramsValue = new Object[argumentTypes.length];
			// if (paramsValue.length > 0) {
			// for (int i = 0; i < paramsValue.length; i++) {
			// if (argumentTypes[i].getSort() == 1) {
			// paramsValue[i] = "";
			// }if (argumentTypes[i].getSort() == 2) {
			// paramsValue[i] = 2;
			// }
			// }
			// }

			// Point point = new Point(this.packageName, this.className,
			// this.methodName, this.methodDescriptor);
			varLocalVariableIndex2 = newLocal(Type.getType(Point.class));
			mv.visitTypeInsn(NEW, "monitor/aop/Point");
			mv.visitInsn(DUP);
			mv.visitLdcInsn(this.packageName);
			mv.visitLdcInsn(this.className);
			mv.visitLdcInsn(this.methodName);
			mv.visitLdcInsn(this.methodDescriptor);
			mv.visitMethodInsn(INVOKESPECIAL, "monitor/aop/Point", "<init>",
					"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ASTORE, varLocalVariableIndex2);
			// point.before(null);
			mv.visitVarInsn(ALOAD, varLocalVariableIndex2);
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKEVIRTUAL, "monitor/aop/Point", "before", "([Ljava/lang/Object;)V", false);

		}
	}

	@Override
	protected void onMethodExit(int opcode) {

		if (!(methodName.equals("<init>") || "<clinit>".equals(methodName))) {
			if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {

				Type type = Type.getReturnType(this.methodDescriptor);
				int typeShort = type.getSort();
				int returnIndex = varLocalVariableIndex2 + 1;
				if (opcode != Opcodes.RETURN) {
					returnIndex = newLocal(type);
					
					if (opcode == Opcodes.IRETURN) {
						mv.visitVarInsn(Opcodes.ISTORE, returnIndex);
						mv.visitVarInsn(Opcodes.ILOAD, returnIndex);
						if (typeShort == 1) {
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;",
									false);
						} else if (typeShort == 2) {
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf",
									"(C)Ljava/lang/Character;", false);
						} else if (typeShort == 3) {
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
						} else if (typeShort == 4) {
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;",
									false);
						} else if (typeShort == 5) {
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;",
									false);
						}
					} else if (opcode == Opcodes.LRETURN) {
						mv.visitVarInsn(Opcodes.LSTORE, returnIndex);
						mv.visitVarInsn(Opcodes.LLOAD, returnIndex);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
					} else if (opcode == Opcodes.FRETURN) {
						mv.visitVarInsn(Opcodes.FSTORE, returnIndex);
						mv.visitVarInsn(Opcodes.FLOAD, returnIndex);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
					} else if (opcode == Opcodes.DRETURN) {
						mv.visitVarInsn(Opcodes.DSTORE, returnIndex);
						mv.visitVarInsn(Opcodes.DLOAD, returnIndex);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
					} else if (opcode == Opcodes.ARETURN) {
						mv.visitVarInsn(Opcodes.ASTORE, returnIndex);
						mv.visitVarInsn(Opcodes.ALOAD, returnIndex);
					}
//					mv.visitVarInsn(Opcodes.ASTORE, returnIndex+1);
					// Point point = new Point();
					// point.after(1);
					// point.after(null);

					mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
					mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
					mv.visitInsn(Opcodes.DUP);
					mv.visitLdcInsn("return:");
					mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>",
							"(Ljava/lang/String;)V", false);

					// append
					if (opcode == Opcodes.IRETURN) {
						mv.visitVarInsn(Opcodes.ILOAD, returnIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(I)Ljava/lang/StringBuilder;", false);
					} else if (opcode == Opcodes.LRETURN) {
						mv.visitVarInsn(Opcodes.LLOAD, returnIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(J)Ljava/lang/StringBuilder;", false);
					} else if (opcode == Opcodes.FRETURN) {
						mv.visitVarInsn(Opcodes.FLOAD, returnIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(F)Ljava/lang/StringBuilder;", false);
					} else if (opcode == Opcodes.DRETURN) {
						mv.visitVarInsn(Opcodes.DLOAD, returnIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(D)Ljava/lang/StringBuilder;", false);
					} else if (opcode == Opcodes.ARETURN) {
						mv.visitVarInsn(Opcodes.ALOAD, returnIndex);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
								"(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
					}
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;",
							false);
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);


					mv.visitVarInsn(ALOAD, varLocalVariableIndex2);
//					mv.visitVarInsn(Opcodes.ALOAD, returnIndex+1);
					mv.visitInsn(ACONST_NULL);
					mv.visitMethodInsn(INVOKEVIRTUAL, "monitor/aop/Point", "after", "(Ljava/lang/Object;)V", false);

					// 把返回值重新写入栈顶
					if (opcode == Opcodes.IRETURN) {
						mv.visitVarInsn(Opcodes.ILOAD, returnIndex);
					} else if (opcode == Opcodes.LRETURN) {
						mv.visitVarInsn(Opcodes.LLOAD, returnIndex);
					} else if (opcode == Opcodes.FRETURN) {
						mv.visitVarInsn(Opcodes.FLOAD, returnIndex);
					} else if (opcode == Opcodes.DRETURN) {
						mv.visitVarInsn(Opcodes.DLOAD, returnIndex);
					} else if (opcode == Opcodes.ARETURN) {
						mv.visitVarInsn(Opcodes.ALOAD, returnIndex);
					}
				}
			}
		}
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {

		super.visitMaxs(maxStack + 4, maxLocals);

	}
}
