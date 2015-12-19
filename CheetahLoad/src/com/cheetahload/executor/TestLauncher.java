package com.cheetahload.executor;

import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.TestSuite;
import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.CommonLoggerWriter;
import com.cheetahload.log.Level;
import com.cheetahload.log.UserLoggerWriter;
import com.cheetahload.timer.TimerWriter;

public final class TestLauncher {

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

	public static void stopLogger() {
		timerWriter.setStopSignal(true);
		userLoggerWriter.setStopSignal(true);
		commonLoggerWriter.setStopSignal(true);
		while (timerWriter.isAlive() || userLoggerWriter.isAlive() || commonLoggerWriter.isAlive()) { 
			try{
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Test is completed.");
	}

	public final static void run(TestSuite testSuite) {
		TestResult result = TestResult.getTestResult();
		TestConfiguration config = TestConfiguration.getTestConfiguration();

		if (!config.verifyConfiguration()) {
			CommonLogger.getCommonLogger().write(
					"TestEntry - runTest() Test configuration settings are not completed. Test can't start! ",
					Level.ERROR);
			commonLoggerWriter.setStopSignal(true);
			System.exit(0);
		}

		startLogger();
		System.out.println("Test is starting...");
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
			CommonLogger.getCommonLogger().write(
					"TestEntry - runTest() Thread can't be started. Error: " + e.getStackTrace().toString(),
					Level.ERROR);
		}

		// output statistic data
		CommonLogger.getCommonLogger().write("TestEntry - runTest() Execution Summary Start.", Level.INFO);
		Hashtable<String, Integer> userExecutionCountTable = result.getUserExecutionCountTable();
		for (String key : userExecutionCountTable.keySet()) {
			CommonLogger.getCommonLogger().write(
					"TestEntry - runTest() " + key + " execution count: " + userExecutionCountTable.get(key),
					Level.INFO);
		}

		Hashtable<String, Integer> userErrorCountTable = result.getUserErrorCountTable();
		if (userErrorCountTable.isEmpty()) {
			CommonLogger.getCommonLogger().write("TestEntry - runTest() There is no error in test.", Level.INFO);
		} else {
			for (String key : userErrorCountTable.keySet()) {
				CommonLogger.getCommonLogger().write(
						"TestEntry - runTest() " + key + " error count: " + userErrorCountTable.get(key), Level.INFO);
			}
		}

		CommonLogger.getCommonLogger().write("TestEntry - runTest() Execution Summary End.", Level.INFO);
		stopLogger();
	}
}
