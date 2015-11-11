package monitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import monitor.aop.ClassesChoose;
import test.Test1;
import test.Test2;

public class Properties {

	private static String monitorServerURL;

	// 改成内部配置文件的方式
	private static final String[] monitorPrivatePatterns = new String[] {};

	public Properties() {

		monitorServerURL = "";
	}

	public String getMonitorServerURL() {
		return monitorServerURL;
	}

	public void setMonitorServerURL(String monitorServerURL) {
		this.monitorServerURL = monitorServerURL;
	}

	public static void doProperties(String propertiesFilePath) {

		Map<String, String> propertiesMap = new HashMap<String, String>();
		java.util.Properties pps = new java.util.Properties();
		try {
			pps.load(new FileInputStream(propertiesFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Enumeration<?> enum1 = pps.propertyNames();// 得到配置文件的名字
		while (enum1.hasMoreElements()) {
			String strKey = (String) enum1.nextElement();
			String strValue = pps.getProperty(strKey);
			propertiesMap.put(strKey, strValue);
		}

		// monitorPatterns
		String monitorPatternsString = propertiesMap.get("monitorPatterns");
		String[] monitorPatternsStrings = monitorPatternsString.split(",");
		String[] monitorPatterns = new String[monitorPatternsStrings.length];
		for (int i = 0; i < monitorPatternsStrings.length; i++) {
			monitorPatterns[i] = propertiesMap.get(monitorPatternsStrings[i]);
		}
		Pattern[] patterns = new Pattern[monitorPatterns.length];
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = Pattern.compile(monitorPatterns[i]);
		}
		ClassesChoose.setPatterns(patterns);

		// monitorServerURL
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String path = Properties.class.getResource("/").getPath();
		String propertiesFilePath = path + "test.properties";
		doProperties(propertiesFilePath);

		Class<?>[] classes = new Class<?>[] { List.class, Test1.class, Test2.class, Properties.class };
		classes = ClassesChoose.chooseClasses(classes);
		for (int i = 0; i < classes.length; i++) {
			System.out.println(classes[i].getName());
		}
	}
}
