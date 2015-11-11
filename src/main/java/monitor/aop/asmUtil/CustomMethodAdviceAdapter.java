package monitor.aop.asmUtil;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import monitor.Application;
import monitor.aop.Point;

public class CustomMethodAdviceAdapter extends AdviceAdapter {

	private int varLocalVariableIndex;

	private final String packageName;

	private final String className;

	private String methodName;

	private final String methodDescriptor;

	/**
	 * 是否是静态方法
	 */
	private final boolean IS_STATIC;

	public CustomMethodAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc) {

		super(api, mv, access, name, desc);
		this.methodName = name;
		this.methodDescriptor = desc;
		this.packageName = Application.packageName.get();
		this.className = Application.className.get();
		// 判断是否是静态方法
		if ((access & Opcodes.ACC_STATIC) == 0) {
			IS_STATIC = false;
		} else {
			IS_STATIC = true;
		}
	}

	@Override
	protected void onMethodEnter() {
		// super.visitCode();

		if (!(methodName.equals("<init>") || "<clinit>".equals(methodName))) {

			// Point point = new Point(this.packageName, this.className,
			// this.methodName, this.methodDescriptor);
			varLocalVariableIndex = newLocal(Type.getType(Point.class));
			mv.visitTypeInsn(NEW, "monitor/aop/Point");
			mv.visitInsn(DUP);
			mv.visitLdcInsn(this.packageName);
			mv.visitLdcInsn(this.className);
			mv.visitLdcInsn(this.methodName);
			mv.visitLdcInsn(this.methodDescriptor);
			mv.visitMethodInsn(INVOKESPECIAL, "monitor/aop/Point", "<init>",
					"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ASTORE, varLocalVariableIndex);

			// 注册切面到切面链中
			// Point point = new Point();
			// point.register(MethodTimeCount.class);
			// point.register(Class<? extends AspectPoint> aspectPointClass);
			mv.visitVarInsn(ALOAD, varLocalVariableIndex);
			mv.visitLdcInsn(Type.getType("Lmonitor/support/MethodAnalyze;"));
			mv.visitMethodInsn(INVOKEVIRTUAL, "monitor/aop/Point", "register", "(Ljava/lang/Class;)V", false);

			// 完成point.before(Object[] o)的参数组装
			mv.visitVarInsn(ALOAD, varLocalVariableIndex);
			Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);
			if (argumentTypes.length > 0) {// 组织Object[]，把方法请求参数放入到这个object[]中，传递给切面链
				// 声明object[]
				if (argumentTypes.length < 5) {
					switch (argumentTypes.length) {
					case 0:
						mv.visitInsn(ICONST_0);
						break;
					case 1:
						mv.visitInsn(ICONST_1);
						break;
					case 2:
						mv.visitInsn(ICONST_2);
						break;
					case 3:
						mv.visitInsn(ICONST_3);
						break;
					case 4:
						mv.visitInsn(ICONST_4);
						break;
					case 5:
						mv.visitInsn(ICONST_5);
						break;
					}
				} else {
					mv.visitIntInsn(BIPUSH, argumentTypes.length);
				}
				mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
				mv.visitInsn(DUP);

				for (int i = 0; i < argumentTypes.length; i++) {
					// 改变数组下标
					if (i < 5) {
						switch (i) {
						case 0:
							mv.visitInsn(ICONST_0);
							break;
						case 1:
							mv.visitInsn(ICONST_1);
							break;
						case 2:
							mv.visitInsn(ICONST_2);
							break;
						case 3:
							mv.visitInsn(ICONST_3);
							break;
						case 4:
							mv.visitInsn(ICONST_4);
							break;
						}
					} else {
						mv.visitIntInsn(BIPUSH, i);
						break;
					}

					// 把参数转换成对象类型插入数组下标的位置中
					int index = i;
					if (!IS_STATIC) {// 如果方法是静态方法，方法的参数从0开始，如果不是，从1开始，0的位置为this
						index = i + 1;
					}
					int typeShort = argumentTypes[i].getSort();
					if (typeShort == Type.BOOLEAN) {
						mv.visitVarInsn(ILOAD, index);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;",
								false);
					} else if (typeShort == Type.CHAR) {
						mv.visitVarInsn(ILOAD, index);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;",
								false);
					} else if (typeShort == Type.BYTE) {
						mv.visitVarInsn(ILOAD, index);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
					} else if (typeShort == Type.SHORT) {
						mv.visitVarInsn(ILOAD, index);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
					} else if (typeShort == Type.INT) {
						mv.visitVarInsn(ILOAD, index);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;",
								false);

						// 前五种数据类型用ILOAD可以读取，后面的每种有另外的规则
					} else if (typeShort == Type.LONG) {
						mv.visitVarInsn(LLOAD, index);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
					} else if (typeShort == Type.FLOAT) {
						mv.visitVarInsn(FLOAD, index);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
					} else if (typeShort == Type.DOUBLE) {
						mv.visitVarInsn(DLOAD, index);
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
					} else if (typeShort == Type.ARRAY) {
						mv.visitVarInsn(ALOAD, index);
					} else if (typeShort == Type.OBJECT) {
						mv.visitVarInsn(ALOAD, index);
					} else if (typeShort == Type.METHOD) {
						mv.visitVarInsn(ALOAD, index);
					}

					if (i < argumentTypes.length - 1) {
						mv.visitInsn(AASTORE);
						mv.visitInsn(DUP);
					}
				}
				mv.visitInsn(AASTORE);
			} else {// 如果没有参数，传递null
				mv.visitInsn(ACONST_NULL);
			}

			// 执行point.before(Object[] o)
			mv.visitMethodInsn(INVOKEVIRTUAL, "monitor/aop/Point", "before", "([Ljava/lang/Object;)V", false);
			
			// mv.visitLocalVariable("return_", type.getClassName(),
			// null, new Label(), new Label(), returnIndex);
			mv.visitLocalVariable("monitorAopPoint$_agent", "Lmonitor/aop/Point;", null, new Label(),
					new Label(), varLocalVariableIndex);
		}
	}

	@Override
	protected void onMethodExit(int opcode) {

		if (!(methodName.equals("<init>") || "<clinit>".equals(methodName))) {
			if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {

				Type type = Type.getReturnType(this.methodDescriptor);
				int typeShort = type.getSort();
				int returnIndex = 0;
				int returnIndex_ = 0;
				if (opcode != Opcodes.RETURN && opcode != Opcodes.ATHROW) {// 如果方法有返回值，不为void，则把返回值传递给切面链并把返回值重写写入栈顶让反正正常结束
					returnIndex = newLocal(type);

					// 把返回值转换成引用数据类型
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

					returnIndex_ = newLocal(type);
					mv.visitVarInsn(Opcodes.ASTORE, returnIndex_);
					// 执行 point.after(null);
					mv.visitVarInsn(ALOAD, varLocalVariableIndex);
					mv.visitVarInsn(Opcodes.ALOAD, returnIndex_);
					mv.visitMethodInsn(INVOKEVIRTUAL, "monitor/aop/Point", "after", "(Ljava/lang/Object;)V", false);

					// 把返回值重新写入栈顶，让方法正常结束
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
				} else if (opcode == Opcodes.ATHROW) {
					// 抛出异常比较复杂，会出现两次ATHROW指令，暂时不处理方法发生异常的情况，通过重写Throwable的构造器来实现异常监控

					// returnIndex = newLocal(Type.getType(Exception.class));
					// mv.visitVarInsn(Opcodes.ASTORE, returnIndex);
					// mv.visitVarInsn(Opcodes.ALOAD, returnIndex);
					// 执行 point.after(exception);
					// mv.visitVarInsn(ALOAD, varLocalVariableIndex);
					// mv.visitVarInsn(Opcodes.ALOAD, returnIndex);
					// mv.visitMethodInsn(INVOKEVIRTUAL, "monitor/aop/Point",
					// "after", "(Ljava/lang/Object;)V", false);

					// 把返回值重新写入栈顶，让方法正常结束
					// mv.visitVarInsn(Opcodes.ALOAD, returnIndex);
				} else {
					mv.visitVarInsn(ALOAD, varLocalVariableIndex);
					mv.visitInsn(ACONST_NULL);
					mv.visitMethodInsn(INVOKEVIRTUAL, "monitor/aop/Point", "after", "(Ljava/lang/Object;)V", false);
				}
			}
		}
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		if (maxStack > 1) {
			super.visitMaxs(maxStack, maxLocals + 3);
		} else {
			super.visitMaxs(maxStack + 1, maxLocals + 3);
		}
	}
}
