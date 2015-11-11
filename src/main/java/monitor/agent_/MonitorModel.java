package monitor.agent_;

public class MonitorModel {

	private String className;
	private String methodName;
	private Object[] methodParams;
	private String[] methodParamsType;
	private long startTime;
	private long endTime;
	private long time;

	private String s;

	public MonitorModel() {
		this.before();
	}

	public void before() {
		startTime = System.nanoTime();
	}

	public void after() {
		endTime = System.nanoTime();
		time = endTime - startTime;
		System.out.println("这个方法的运行时间:" + time / 1000 / 1000 + "ms,从" + startTime + "开始运行。");
	}

	public void getString() {

		System.out.println("MonitorModel>>:" + s);
	}

	public void getString2() {

		System.out.println("MonitorModel2 >>:" + s);
	}

	public static void main(String[] args) {

		MonitorModel localMonitorModel = new MonitorModel();
		localMonitorModel.getString();
		int a = 2;
		int b = 3;
		int c = 4;
		int a1 = 2;
	}

	public static int getNumber() {
		MonitorModel localMonitorModel = new MonitorModel();
		localMonitorModel.getString();
		int a = 2;
		int b = 3;
		int c = 4;
		System.out.println(a);
		System.out.println(2);

		System.out.println(22);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(3);
		localMonitorModel.getString();
		return getNumber2();

	}

	public static int getNumber2() {

		System.out.println("");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

}
