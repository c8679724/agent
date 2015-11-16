package monitor.aop;

import java.util.List;

import org.junit.Test;

import monitor.Properties;
import test.Test1;
import test.Test2;

public class TestClassesChoose {

	@Test
	public void chooseClasses() {
		String path = Properties.class.getResource("/").getPath();
		String propertiesFilePath = path + "test.properties";
		Properties.doMonitorSystemProperties(propertiesFilePath);

		// Class<?>[] classes = new Class<?>[] {
		// sun.text.resources.FormatData_zh.class, List.class, Test1.class,
		// Test2.class, Properties.class };

		Class<?>[] classes = new Class<?>[] { List.class, Test1.class, Test2.class, Properties.class };
		classes = ClassesChoose.chooseClasses(classes);
		for (int i = 0; i < classes.length; i++) {
			System.out.println(classes[i].getName());
		}
	}
}
