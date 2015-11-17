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

	public Properties() {

		monitorServerURL = "";
	}

	public String getMonitorServerURL() {
		return monitorServerURL;
	}

	public static void doMonitorSystemProperties(String propertiesFilePath) {

		Map<String, String> propertiesMap = new HashMap<String, String>();
		java.util.Properties pps = new java.util.Properties();
		try {
			pps.load(Properties.class.getResourceAsStream(propertiesFilePath));
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

		// exPatterns
		String exPatterns_ = propertiesMap.get("exPatterns");
		String[] exPatternsStrings = exPatterns_.split(",");
		String[] exPatterns = new String[exPatternsStrings.length];
		for (int i = 0; i < exPatternsStrings.length; i++) {
			exPatterns[i] = propertiesMap.get(exPatternsStrings[i]);
		}
		Pattern[] patterns = new Pattern[exPatterns.length];
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = Pattern.compile(exPatterns[i]);
		}
		ClassesChoose.setJdkPatterns(patterns);

		// inPatterns
		String inPatterns_ = propertiesMap.get("inPatterns");
		String[] inPatternsStrings = inPatterns_.split(",");
		String[] inPatterns = new String[inPatternsStrings.length];
		for (int i = 0; i < inPatternsStrings.length; i++) {
			inPatterns[i] = propertiesMap.get(inPatternsStrings[i]);
		}
		Pattern[] patterns2 = new Pattern[inPatterns.length];
		for (int i = 0; i < patterns2.length; i++) {
			patterns2[i] = Pattern.compile(inPatterns[i]);
		}
		ClassesChoose.setInPatterns(patterns2);
	}

	public static void doUserProperties(String jarFilePath) {

		Map<String, String> propertiesMap = new HashMap<String, String>();
		java.util.Properties pps = new java.util.Properties();
		try {
			pps.load(new FileInputStream(jarFilePath + "/monitor.properties"));
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
		String propertiesFilePath = "/monitor.properties";
		doMonitorSystemProperties(propertiesFilePath);

		Class<?>[] classes = new Class<?>[] { List.class, Test1.class, Test2.class, Properties.class };
		classes = ClassesChoose.chooseClasses(classes);
		for (int i = 0; i < classes.length; i++) {
			System.out.println(classes[i].getName());
		}
	}
}
