package com.cheetahload.executor;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.TestSuite;
import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.CommonLoggerWriter;
import com.cheetahload.log.Level;
import com.cheetahload.log.UserLoggerWriter;
import com.cheetahload.timer.TimerWriter;

public final class TestEntry {
	
	private static CommonLoggerWriter commonLoggerWriter;
	private static TimerWriter timerWriter;
	private static UserLoggerWriter userLoggerWriter;

	private static void startLogger() {
		commonLoggerWriter = new CommonLoggerWriter();
		commonLoggerWriter.start();

		timerWriter = new TimerWriter();
		timerWriter.start();

		userLoggerWriter = new UserLoggerWriter();
		userLoggerWriter.start();
	}

	private static void stopLogger() {
		timerWriter.setStopSignal(true);
		userLoggerWriter.setStopSignal(true);
		commonLoggerWriter.setStopSignal(true);
	}

	public final static void runTest(TestSuite testSuite) {
		TestResult result = TestResult.getTestResult();
		TestConfiguration config = TestConfiguration.getTestConfiguration();

		if (!config.verifyConfiguration()) {
			CommonLogger.getCommonLogger().write("TestEntry - runTest() Test configuration settings are not completed. Test can't start! ",
					Level.ERROR);
			commonLoggerWriter.setStopSignal(true);
			System.exit(0);
		}
		
		startLogger();

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
			CommonLogger.getCommonLogger()
					.write("TestEntry - runTest() Thread can't be started. Error: " + e.getStackTrace().toString(), Level.ERROR);
		}

		// output statistic data
		CommonLogger.getCommonLogger().write("TestEntry - runTest() Execution Count:", Level.INFO);
		HashMap<String, Integer> userExecutionCountMap = result.getUserExecutionCountMap();
		for (String key : userExecutionCountMap.keySet()) {
			CommonLogger.getCommonLogger().write("TestEntry - runTest() " + key + ": " + userExecutionCountMap.get(key), Level.INFO);
		}

		HashMap<String, Integer> userErrorCountMap = result.getUserErrorCountMap();
		if (userErrorCountMap.isEmpty()) {
			CommonLogger.getCommonLogger().write("TestEntry - runTest() There is no error in test.", Level.INFO);
		} else {
			CommonLogger.getCommonLogger().write("TestEntry - runTest() Error Count:", Level.INFO);
			for (String key : userErrorCountMap.keySet()) {
				CommonLogger.getCommonLogger().write("TestEntry - runTest() " + key + ": " + userErrorCountMap.get(key), Level.INFO);
			}
		}

		stopLogger();
	}
}
