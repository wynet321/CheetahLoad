package com.cheetahload.executor;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestCase;
import com.cheetahload.TestConfiguration;
import com.cheetahload.TestSuite;
import com.cheetahload.log.Level;
import com.cheetahload.timer.TimerWriter;

public final class TestEntry {

	public final static void runTest(TestSuite testSuite) {
		if (!TestConfiguration.isCompleted()) {
			TestConfiguration.getCommonLogger().write(
					"TestEntry - runTest() Test configuration settings are not completed. Test can't start! ",
					Level.ERROR);
			TestConfiguration.getCommonLogger().flush(true);
			System.exit(0);
		}

		// timer log thread
		Iterator<TestCase> iterator = testSuite.getTestCaseList().iterator();
		while (iterator.hasNext()) {
			TestConfiguration.getTimerQueueMap().put(((TestCase) iterator.next()).getTestScript().getName(),
					new ConcurrentLinkedQueue<String>());
		}
		TimerWriter timerWriter = new TimerWriter();
		timerWriter.start();

		// Thread(VU) start
		int threadCount = TestConfiguration.getVusers();
		int i = 0;
		Thread[] thread = new Thread[threadCount];
		while (i < threadCount) {
			thread[i] = new TestThread(testSuite);
			thread[i].start();
			i++;
		}
		timerWriter.setStopSignal(true);
		TestConfiguration.getCommonLogger().flush(true);
		
	}
}
