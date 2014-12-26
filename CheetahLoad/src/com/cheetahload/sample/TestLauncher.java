package com.cheetahload.sample;

import com.cheetahload.TestCase;
import com.cheetahload.TestConfiguration;
import com.cheetahload.TestSuite;
import com.cheetahload.executor.TestEntry;
import com.cheetahload.log.Level;

public class TestLauncher {

	public static void main(String[] args) {
		TestConfiguration.setLoops(2);
		TestConfiguration.setVusers(2, "user_", 2, 1);
		TestConfiguration.setTestSuiteName("Bucket");
		TestConfiguration.setLogLevel(Level.DEBUG);
		
		TestSuite testSuite=new TestSuite();
		testSuite.setPrepareTestCase(new TestScript_Login());
		testSuite.add(new TestCase(new TestScript_Search(),100));
		TestEntry.runTest(testSuite);
	}

}
