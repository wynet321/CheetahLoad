package com.cheetahload.executor;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestSuite;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;

public final class TestEntry {

	public final static void runTest() {
		if (!TestConfiguration.isCompleted()) {
			// TODO log failed
			System.out.println("failed");
			TestConfiguration.getLogger().write("failed1111", Level.ERROR);
			System.exit(0);
		}
		TestSuite testSuite = new TestSuite();
		int threadCount = 1;
		int i = 0;
		Thread[] thread = new Thread[2];
		while (i <= threadCount) {
			thread[i] = new TestThread(testSuite);
			thread[i].start();
			i++;
		}
	}

}
