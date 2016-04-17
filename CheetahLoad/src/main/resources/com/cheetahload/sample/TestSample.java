package main.resources.com.cheetahload.sample;

import main.resources.com.cheetahload.TestCase;
import main.resources.com.cheetahload.TestConfiguration;
import main.resources.com.cheetahload.TestSuite;
import main.resources.com.cheetahload.executor.TestLauncher;
import main.resources.com.cheetahload.log.Level;

public class TestSample {

	public static void main(String[] args) {
		test();
	}

	private static void test() {
		TestConfiguration config = TestConfiguration.getTestConfiguration();
		config.setWholeTestDuration(1);
		config.setUserNames(10, "user_", 2, 1);
		config.setTestSuiteName("Bucket");
		config.setLogLevel(Level.DEBUG);
		config.setTestName("3hrPerformanceRegression");
		config.setTesterName("Dennis Wang");
		config.setTesterMail("wynet321@163.com");
		config.setLogWriteCycleTime(15);
		config.setDbParameters(new String[] { "com.mysql.jdbc.Driver", "jdbc:mysql://100.200.1.19:3306/cheetahdb", "dennis","passw0rd","1" });

		TestSuite testSuite = new TestSuite();
		testSuite.setPrepareTestScript(new TestScript_Login());
		testSuite.add(new TestCase(new TestScript_Search(), 1));
		testSuite.add(new TestCase(new TestScript_Login(), 2));
		TestLauncher.start(testSuite);
	}

}
