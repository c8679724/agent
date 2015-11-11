package monitor.support;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;

import monitor.Application;
import monitor.aop.AspectPoint;
import monitor.aop.JoinPoin;
import monitor.aop.MethodNode;

/**
 * 方法的时间统计支持
 * 
 * @author sky
 *
 */
public class MethodAnalyze2 extends AspectPoint {

	/**
	 * 方法执行时间
	 */
	private long time;

	private long fathterMethodRunIndex;

	private long run_index;

	@Override
	public String[] setPatterns() {
		return null;
	}

	@Override
	public void before(JoinPoin joinPoin) {
		time = System.nanoTime();

		// 方法执行树生成
		MethodNode methodNodeTree = Application.methodNodeTree.get();

		fathterMethodRunIndex = Application.fathterMethodRunIndex.get() == null ? 0
				: Application.fathterMethodRunIndex.get();
		run_index = Application.getMethodRunIndex(fathterMethodRunIndex);

		// 包名.类名.方法名(参数类型...）;
		String methodName = joinPoin.getPackageName() + "." + joinPoin.getClassName() + "." + joinPoin.getMethodName()
				+ "(";
		Type type = null;
		for (int i = 0; i < joinPoin.getMethodParamsType().length; i++) {
			type = joinPoin.getMethodParamsType()[i];
			methodName += type.getClassName();
			if (i < joinPoin.getMethodParamsType().length - 1) {
				methodName += ",";
			}
		}
		methodName += ");";

		if (methodNodeTree == null) {
			methodNodeTree = new MethodNode();
			methodNodeTree.setRun_index(run_index);
			methodNodeTree.setMethodName(methodName);
		} else {
			MethodNode thisMethodNode = new MethodNode();
			thisMethodNode.setRun_index(run_index);
			thisMethodNode.setMethodName(methodName);
			insertChildMethodNode(methodNodeTree, fathterMethodRunIndex, thisMethodNode);
		}
		Application.methodNodeTree.set(methodNodeTree);
		Application.fathterMethodRunIndex.set(run_index);
	}

	/**
	 * 在方法节点树种的父节点下插入一个子节点,用了递归
	 * 
	 * @param methodNode
	 * @param fathterMethodRunIndex
	 * @param childMethodNode
	 */
	private void insertChildMethodNode(MethodNode methodNode, long fathterMethodRunIndex, MethodNode childMethodNode) {
		List<MethodNode> childrenMethodNode = methodNode.getMethodNodeChildren();
		if (childrenMethodNode == null) {
			childrenMethodNode = new ArrayList<MethodNode>();
		}

		if (methodNode.getRun_index() == fathterMethodRunIndex) {
			childrenMethodNode.add(childMethodNode);
		} else {
			for (MethodNode methodNode_ : childrenMethodNode) {
				insertChildMethodNode(methodNode_, fathterMethodRunIndex, childMethodNode);
			}
		}
		methodNode.setMethodNodeChildren(childrenMethodNode);
	}

	@Override
	public void after(JoinPoin joinPoin) {
		time = System.nanoTime() - time;
		System.out.println(joinPoin.getMethodName() + "方法运行时间：" + divide(time, 1000000) + "ms");
		Application.fathterMethodRunIndex.set(fathterMethodRunIndex);
	}

	/**
	 * 精确除法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double divide(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, 3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
