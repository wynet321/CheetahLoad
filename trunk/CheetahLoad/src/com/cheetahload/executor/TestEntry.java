package com.cheetahload.executor;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestSuite;
import com.cheetahload.log.Level;

public final class TestEntry {

	public final static void runTest(TestSuite testSuite) {
		if (!TestConfiguration.isCompleted()) {
			TestConfiguration.getCommonLogger().write(
					"TestEntry - runTest() Test configuration settings are not completed. Test can't start! ",
					Level.ERROR);
			TestConfiguration.getCommonLogger().flush(true);
			System.exit(0);
		}

		int threadCount = TestConfiguration.getVusers();
		int i = 0;
		Thread[] thread = new Thread[threadCount];
		while (i < threadCount) {
			thread[i] = new TestThread(testSuite);
			thread[i].start();
			i++;
		}
		TestConfiguration.getCommonLogger().flush(true);
	}

}
