package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode_;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode_;
import org.objectweb.asm.tree.ParameterNode;

public class App3 extends ClassLoader {
	
	public void getTest(){}
	
	public static void main(String[] args) throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, SecurityException, InstantiationException, NoSuchMethodException {

		// Test1.class.getMethods()[1].invoke(Test1.class.newInstance(), new
		// Object[] {});

		ClassReader cr = new ClassReader(Test1.class.getName());
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		ClassNode_ cn = new ClassNode_(Opcodes.ASM5);
		cr.accept(cn, 0);
		List<MethodNode_> mds = cn.methods;
		for (MethodNode_ methodNode : mds) {
			System.out.println(methodNode.name + "-->" + methodNode.maxLocals);
			if (!(methodNode.name.equals("<init>") || "<clinit>".equals(methodNode.name))) {

				List<LocalVariableNode> vars = methodNode.localVariables;
				/*
				 * InsnList beforeCode = new InsnList();
				 * 
				 * beforeCode.add(new FieldInsnNode(Opcodes.GETSTATIC,
				 * "java/lang/System", "out", "Ljava/io/PrintStream;"));
				 * beforeCode.add(new LdcInsnNode("onMethodEnter-> " +
				 * methodNode.name)); beforeCode.add(new
				 * MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				 * "println", "(Ljava/lang/String;)V", false));
				 * 
				 * beforeCode.add(new TypeInsnNode(Opcodes.NEW,
				 * "monitor/agent/MonitorModel")); beforeCode.add(new
				 * InsnNode(Opcodes.DUP)); beforeCode.add(new
				 * MethodInsnNode(Opcodes.INVOKESPECIAL,
				 * "monitor/agent/MonitorModel", "<init>", "()V", false));
				 * beforeCode.add(new VarInsnNode(Opcodes.ASTORE, 1000));
				 * methodNode.instructions.insert(beforeCode);
				 */

				/*
				 * InsnList aftercode = null; for (int i = 0; i <
				 * methodNode.instructions.size(); i++) { AbstractInsnNode
				 * instruction = methodNode.instructions.get(i); if (instruction
				 * != null) { if ((instruction.getOpcode() >= Opcodes.IRETURN &&
				 * instruction.getOpcode() <= Opcodes.RETURN) ||
				 * instruction.getOpcode() == Opcodes.ATHROW) {
				 * 
				 * aftercode = new InsnList(); aftercode.add(new
				 * VarInsnNode(Opcodes.ALOAD, 1000)); aftercode.add(new
				 * MethodInsnNode(Opcodes.INVOKEVIRTUAL,
				 * "monitor/agent/MonitorModel", "after", "()V", false));
				 * 
				 * aftercode.add(new FieldInsnNode(Opcodes.GETSTATIC,
				 * "java/lang/System", "out", "Ljava/io/PrintStream;"));
				 * aftercode.add(new LdcInsnNode("onMethodExit-> " +
				 * methodNode.name)); aftercode.add(new
				 * MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				 * "println", "(Ljava/lang/String;)V", false)); //
				 * aftercode.add(instruction); //
				 * methodNode.instructions.remove(instruction);
				 * methodNode.instructions.insert(aftercode); } } }
				 */

				for (LocalVariableNode localVariableNode : vars) {
					System.out.println("        var:" + localVariableNode.name + "--" + localVariableNode.desc);
				}
			}
		}

		// CustomVisitor3 myv = new CustomVisitor3(Opcodes.ASM5, cw);

		cn.accept(cw);
		;

		byte[] code = cw.toByteArray();
		File file = new File("h:/test/asm/Test1.class");
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(file);
			fout.write(code);
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 自定义加载器
		App3 loader = new App3();
		Class<?> appClass = loader.defineClass(null, code, 0, code.length);
		appClass.getMethods()[0].invoke(appClass.newInstance(), new Object[] {});

		// FileOutputStream f=new FileOutputStream(new
		// File("d:"+File.separator+"ok2.class"));
		// f.write(code);;
		// f.close();

	}
}
