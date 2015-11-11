package test;

import java.util.List;
import java.util.Map;

public class Test1 {

	/*
	 * public static void main(String[] args) { long time =
	 * System.currentTimeMillis(); new Test1().test(); System.out.println(
	 * "run time:" + (System.currentTimeMillis() - time) + "ms"); }
	 */

	public void test() {
		System.out.println("test");
		int a = 2;
		System.out.println(a);
		new Test1().test0("chengtianhua");
		Test1.test1("chengtianhua");
		Test1.test2(new String[] { "tianhua" }, 1);
		Test1.test3("tianhua", 1, 2);
		Test1.test4();
		Test1.test5();
		Test1.test6();
		Test1.test7();
		System.out.println("test");
	}

	public synchronized final void test0(String a) {
		System.out.println("test1");
		System.out.println(a);
		Throwable b = new Throwable();
		System.out.println("t" + b);
		System.out.println("test1");
		Test1.test1("chengtianhua1");
	}

	public static void test1(String a) {
		System.out.println("test1");
		System.out.println(a);
		Throwable b = new Throwable();
		System.out.println("t" + b);
		System.out.println("test1");
	}

	public static int test2(Object[] name, Integer a) {

		System.out.println("test2");
		System.out.println(name);
		System.out.println("test2");
		return 11;
	}

	public static void test3(String a, int c, long d) {
		int f = 0;
		System.out.println(f);
		System.out.println("test1");
		System.out.println(a);
		int b = 1;
		System.out.println(b);
		System.out.println("test1");
	}

	public static void test3(List<Map<String, String>> a) {
		int f = 0;
		System.out.println(f);
		System.out.println("test1");
		System.out.println(a);
		int b = 1;
		System.out.println(b);
		System.out.println("test1");
	}

	public static String test4() {
		int f = 0;
		System.out.println(f);
		System.out.println("test1");
		int b = 1;
		System.out.println(b);
		System.out.println("test1");

		return "test4";
	}

	public static long test5() {
		int f = 2;
		System.out.println(f + "1");
		System.out.println("test1");
		int b = 1;
		System.out.println(b);
		System.out.println("test1");
		System.out.println();
		return 12;
	}
	//
	// public static long test51() {
	// Point localPoint = new Point("test", "Test1", "test5", "()J");
	// localPoint.before(null);
	// int f = 0;
	// System.out.println(f);
	// System.out.println("test1");
	// int b = 1;
	// System.out.println(b);
	// System.out.println("test1");
	// long l = 12L;
	// Long localLong = Long.valueOf(l);
	// localPoint.after(localLong);
	// return l;
	// }

	public static float test6() {
		int f = 0;
		System.out.println(f);
		System.out.println("test1");
		int b = 1;
		System.out.println(b);
		System.out.println("test1");
		return 12;
	}

	public static double test7() {
		int f = 0;
		System.out.println(f);
		System.out.println("test1");
		int b = 1;
		System.out.println(b);
		System.out.println("test1");
		test8(1);
		return 12;
	}

	public static void test8(int b) {
		if (b == 2) {
			try {
				throw new Exception("test");
			} catch (Exception e) {
			} finally {
				System.out.println();
			}
			System.out.println();
			return;
		}else{
			System.out.println();
			return;
		}
	}
}
