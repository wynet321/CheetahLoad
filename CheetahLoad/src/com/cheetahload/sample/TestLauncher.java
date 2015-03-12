package com.cheetahload.sample;

import com.cheetahload.TestCase;
import com.cheetahload.TestConfiguration;
import com.cheetahload.TestSuite;
import com.cheetahload.executor.TestEntry;
import com.cheetahload.log.Level;

public class TestLauncher {

	public static void main(String[] args) {
		TestConfiguration.setDuration(1);
		TestConfiguration.setVusers(1, "user_", 2, 1);
		TestConfiguration.setTestSuiteName("Bucket");
		TestConfiguration.setLogLevel(Level.DEBUG);

		TestSuite testSuite = new TestSuite();
		//testSuite.setPrepareTestScript(new TestScript_Login());
		testSuite.add(new TestCase(new TestScript_Search(), 1));
		testSuite.add(new TestCase(new TestScript_Login(), 2));
		TestEntry.runTest(testSuite);
	}

}
