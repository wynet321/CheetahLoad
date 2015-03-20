package com.cheetahload.executor;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import com.cheetahload.TestCase;
import com.cheetahload.TestConfiguration;
import com.cheetahload.TestScript;
import com.cheetahload.TestSuite;
import com.cheetahload.log.CommonLoggerWriter;
import com.cheetahload.log.Level;
import com.cheetahload.log.UserLoggerWriter;
import com.cheetahload.timer.TimerWriter;

public final class TestEntry {

	public final static void runTest(TestSuite testSuite) {
		CommonLoggerWriter commonLoggerWriter = new CommonLoggerWriter();
		commonLoggerWriter.start();

		TestConfiguration config = TestConfiguration.getTestConfiguration();
		if (!config.verifyConfiguration()) {
			config.getCommonLogger().write("TestEntry - runTest() Test configuration settings are not completed. Test can't start! ", Level.ERROR);
			commonLoggerWriter.setStopSignal(true);
			System.exit(0);
		}

		// timer log thread
		TestScript prepareTestCase = testSuite.getPrepareTestScript();
		if (prepareTestCase != null) {
			config.getTimerQueueMap().put(prepareTestCase.getName(), new ConcurrentLinkedQueue<String>());
		}
		TestScript clearupTestCase = testSuite.getClearupTestScript();
		if (clearupTestCase != null) {
			config.getTimerQueueMap().put(clearupTestCase.getName(), new ConcurrentLinkedQueue<String>());
		}
		Iterator<TestCase> testCaseIterator = testSuite.getTestCaseList().iterator();
		while (testCaseIterator.hasNext()) {
			config.getTimerQueueMap().put(((TestCase) testCaseIterator.next()).getTestScript().getName(), new ConcurrentLinkedQueue<String>());
		}
		TimerWriter timerWriter = new TimerWriter();
		timerWriter.start();

		for (String userName : config.getUserNames()) {
			config.getUserLoggerQueueMap().put(userName, new ConcurrentLinkedQueue<String>());
		}

		UserLoggerWriter userLoggerWriter = new UserLoggerWriter();
		userLoggerWriter.start();

		// Thread(VU) start
		int threadCount = config.getVusers();
		int i = 0;
		Thread[] thread = new Thread[threadCount];
		CountDownLatch threadSignal = new CountDownLatch(threadCount);
		while (i < threadCount) {
			thread[i] = new TestThread(testSuite, threadSignal);
			thread[i].start();
			i++;
		}
		try {
			threadSignal.await();
		} catch (InterruptedException e) {
			config.getCommonLogger().write("TestEntry - runTest() Thread can't be started. Error: " + e.getStackTrace().toString(), Level.ERROR);
		}

		// stop log write threads
		timerWriter.setStopSignal(true);
		userLoggerWriter.setStopSignal(true);
		commonLoggerWriter.setStopSignal(true);
	}
}
