package monitor.aop;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;

import com.google.gson.Gson;

import monitor.Application;
import monitor.support.MethodAnalyze;

public class Point {

	private static final Gson gson = new Gson();

	private static final List<Class<? extends AspectPoint>> ASPECT_POINTS = new ArrayList<Class<? extends AspectPoint>>();

	private List<AspectPoint> aspects = new ArrayList<AspectPoint>();

	private JoinPoin joinPoin;

	public Point() {

	}

	/**
	 * 解析方法字节码获取一些非运行期的参数 </br>
	 * 注：前期因技术问题，先不考虑参数名，暂时还没有用途
	 * 
	 * @param package_
	 *            包名
	 * @param className
	 *            类名
	 * @param methodName
	 *            方法名
	 * @param methodParamsType
	 *            方法参数类型
	 * @param returnType
	 *            返回值类型
	 */
	public Point(String packageName, String className, String methodName, String methodDescriptor) {

		joinPoin = new JoinPoin();
		joinPoin.setPackageName(packageName);
		joinPoin.setClassName(className);
		joinPoin.setMethodName(methodName);
		joinPoin.setMethodParamsType(Type.getArgumentTypes(methodDescriptor));
		joinPoin.setReturnType(Type.getReturnType(methodDescriptor));
	}

	/**
	 * 运行时注册切面，程序自动调用
	 * 
	 * @param method
	 * @param methodParamsName
	 */
	public void register(Class<? extends AspectPoint> aspectPointClass) {
		try {
			// 创建切面类的对象并添加到切面链中
			aspects.add(aspectPointClass.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加切面类
	 * 
	 * @param method
	 * @param methodParamsName
	 */
	public void addAspectPoint(Class<? extends AspectPoint> aspectPointClass) {

		// 校验是否符合要求在添加进切面链
		// 是否具有before、after、setPattern方法

		ASPECT_POINTS.add(aspectPointClass);
	}

	public void before(Object... paramsValue) {
		joinPoin.setParamsValue(paramsValue);

		// 方法执行树生成
		madeMethodNodeTree();

		System.out.println("Point.before()--joinPoin-" + joinPoin.getMethodName() + ":" + gson.toJson(joinPoin));
		System.out.println("Point.before()--paramsValue-" + joinPoin.getMethodName() + ":" + gson.toJson(paramsValue));

		for (AspectPoint aspect : aspects) {
			try {
				aspect.before(joinPoin);
			} catch (Exception e) {
				e.printStackTrace();
				//
			}
		}
	}

	public void after(Object returnValue) {
		joinPoin.setReturnValue(returnValue);

		System.out.println("Point.after()--joinPoin-" + joinPoin.getMethodName() + ":" + gson.toJson(joinPoin));
		System.out.println("Point.after()-->returnValue-" + joinPoin.getMethodName() + ":"
				+ gson.toJson(joinPoin.getReturnValue()));
		System.out.println(gson.toJson(Application.methodNodeTree.get()));

		for (AspectPoint aspect : aspects) {
			try {
				aspect.after(joinPoin);
			} catch (Exception e) {
				//
			}
		}

		Application.fathterMethodRunIndex.set(joinPoin.getFathterMethodRunIndex());
	}

	/**
	 * 创建方法执行树
	 */
	public void madeMethodNodeTree() {
		MethodNode methodNodeTree = Application.methodNodeTree.get();

		joinPoin.setFathterMethodRunIndex(
				Application.fathterMethodRunIndex.get() == null ? 0 : Application.fathterMethodRunIndex.get());
		joinPoin.setRun_index(Application.getMethodRunIndex(joinPoin.getFathterMethodRunIndex()));

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
			methodNodeTree.setRun_index(joinPoin.getRun_index());
			methodNodeTree.setMethodName(methodName);
		} else {
			MethodNode thisMethodNode = new MethodNode();
			thisMethodNode.setRun_index(joinPoin.getRun_index());
			thisMethodNode.setMethodName(methodName);
			insertChildMethodNode(methodNodeTree, joinPoin.getFathterMethodRunIndex(), thisMethodNode);
		}
		Application.methodNodeTree.set(methodNodeTree);
		Application.fathterMethodRunIndex.set(joinPoin.getRun_index());
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

	public static void main(String[] args) {
		Point p = new Point();
		p.register(MethodAnalyze.class);
		p.before("123", 1);
		p.after(1);
	}
}
