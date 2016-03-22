package com.cheetahload.sample;

import com.cheetahload.TestCase;
import com.cheetahload.TestConfiguration;
import com.cheetahload.TestSuite;
import com.cheetahload.executor.TestLauncher;
import com.cheetahload.log.Level;

public class TestSample {

	public static void main(String[] args) {
		test();
	}

	private static void test() {
		TestConfiguration config = TestConfiguration.getTestConfiguration();
		config.setWholeTestDuration(1);
		config.setUserNames(2, "user_", 2, 1);
		config.setTestSuiteName("Bucket");
		config.setLogLevel(Level.DEBUG);
		config.setTestName("3hrPerformanceRegression");
		config.setTesterName("Dennis Wang");
		config.setTesterMail("wynet321@163.com");

		TestSuite testSuite = new TestSuite();
		testSuite.setPrepareTestScript(new TestScript_Login());
		testSuite.add(new TestCase(new TestScript_Search(), 1));
		testSuite.add(new TestCase(new TestScript_Login(), 2));
		TestLauncher.start(testSuite);
	}

}
