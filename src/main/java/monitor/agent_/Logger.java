package monitor.agent_;

public class Logger {

	static boolean swtch = false;
	private static String logger_ = "";

	static {

		new Thread() {
			public void run() {
				while (!swtch) {
					System.out.println(logger_);
					logger_ ="";
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public static void logger(String... logs) {
		String s = "";
		for (int i = 0; i < logs.length; i++) {
			s += logs[i] + "\t";
		}
		System.out.println(s);
	}

	public static void logger(String logs) {
//		System.out.println("run logger");
		logger_ += "logs:>>>" + logs + "\n";
	}

	public static void main(String[] args) {
		logger("");
	}
}
