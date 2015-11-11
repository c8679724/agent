package monitor.agent_;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;

public class Transformer3 implements ClassFileTransformer {

	final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
	final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";
	final static List<String> methodList = new ArrayList<String>();

	static {
		methodList.add("java.lang.Throwable.printStackTrace");
	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		System.out.println("-----------" + className + "--------------");

		if (className.equals("monitor/util/Test1")) {
			ClassReader cr = new ClassReader(classfileBuffer);
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);
			for (Object obj : cn.methods) {
				MethodNode md = (MethodNode) obj;
				if ("<init>".endsWith(md.name) || "<clinit>".equals(md.name)) {
					continue;
				}
				InsnList insns = md.instructions;
				InsnList il = new InsnList();

				il.add(new TypeInsnNode(Opcodes.NEW, "monitor/agent/MonitorModel"));
				il.add(new InsnNode(Opcodes.DUP));
				il.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "monitor/agent/MonitorModel", "<init>", "()V", false));
				il.add(new VarInsnNode(Opcodes.ASTORE, 0));
				il.add(new VarInsnNode(Opcodes.ALOAD, 0));
				il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "monitor/agent/MonitorModel", "getString", "()V",
						false));
				// il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
				// "monitor/agent/MonitorModel", "getString", "()V",
				// false));

				il.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
				il.add(new LdcInsnNode("Enter method-> " + cn.name + "." + md.name));
				il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
						"(Ljava/lang/String;)V", false));
				il.add(new VarInsnNode(Opcodes.ALOAD, 0));
				il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "monitor/agent/MonitorModel", "getString2", "()V",
						false));
				insns.insert(il);
				md.maxStack += 10;
				ListIterator<AbstractInsnNode> insn_its = insns.iterator();
				AbstractInsnNode returnInsnNode = null;

				while (insn_its.hasNext()) {
					returnInsnNode = insn_its.next();
					if (returnInsnNode.getOpcode() > 171 && returnInsnNode.getOpcode() < 178) {
						InsnList afterInsnList = new InsnList();
						afterInsnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
						afterInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "monitor/agent/MonitorModel",
								"getString2", "()V", false));
						insns.insertBefore(returnInsnNode.getPrevious(), afterInsnList);
					}
				}

				// il.add(new FieldInsnNode(Opcodes.GETSTATIC,
				// "java/lang/System", "out", "Ljava/io/PrintStream;"));
				// il.add(new LdcInsnNode("Enter method --> " + cn.name + "." +
				// md.name));
				// il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
				// "java/io/PrintStream", "println",
				// "(Ljava/lang/String;)V", false));
				// md.maxStack += 11;
			}
			ClassWriter cw = new ClassWriter(0);
			// 定义类的属性
			// cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL +
			// Opcodes.ACC_STATIC, "LESS", "I", null,
			// new Integer(-1)).visitEnd();
			cw.visitEnd(); // 使cw类已经完成
			cn.accept(cw);

			byte[] data = cw.toByteArray();
			File file = new File("h:/test/asm/Test1.class");
			FileOutputStream fout;
			try {
				fout = new FileOutputStream(file);
				fout.write(data);
				fout.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return cw.toByteArray();

		} else {

			return classfileBuffer;
		}
	}
}
