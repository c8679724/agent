package test;

public class MonitorFilter {

	public static void main(String[] args) {

		new Test1().test();

		monitor.util.jvm.VirtualMachine.loadAgent("H:/workspace/agent/target/agent-0.0.1-SNAPSHOT.jar");

		new Test1().test();

		Throwable t = new Throwable("Test");

		try {
			throw t;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
