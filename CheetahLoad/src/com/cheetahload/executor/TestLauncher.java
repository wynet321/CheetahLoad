package com.cheetahload.executor;

import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.TestSuite;
import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.LogLevel;

public final class TestLauncher {

	public final static void run(TestSuite testSuite) {
		TestResult result = TestResult.getTestResult();
		result.startLoggerWriters();

		TestConfiguration config = TestConfiguration.getTestConfiguration();
		if (!config.verify()) {
			CommonLogger.getLogger().write(
					"TestEntry - runTest() Test configuration settings are not completed. Test can't start! ",
					LogLevel.ERROR);
			result.stopLoggerWriters();
			System.exit(0);
		}

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
			CommonLogger.getLogger().write(
					"TestEntry - runTest() Thread can't be started. Error: " + e.getStackTrace().toString(),
					LogLevel.ERROR);
		}

		// output statistic data
		CommonLogger.getLogger().write("TestEntry - runTest() Execution Count:", LogLevel.INFO);
		Hashtable<String, Integer> userExecutionCountTable = result.getUserExecutionCountTable();
		for (String key : userExecutionCountTable.keySet()) {
			CommonLogger.getLogger().write("TestEntry - runTest() " + key + ": " + userExecutionCountTable.get(key),
					LogLevel.INFO);
		}

		Hashtable<String, Integer> userErrorCountTable = result.getUserErrorCountTable();
		if (userErrorCountTable.isEmpty()) {
			CommonLogger.getLogger().write("TestEntry - runTest() There is no error in test.", LogLevel.INFO);
		} else {
			CommonLogger.getLogger().write("TestEntry - runTest() Error Count:", LogLevel.INFO);
			for (String key : userErrorCountTable.keySet()) {
				CommonLogger.getLogger().write("TestEntry - runTest() " + key + ": " + userErrorCountTable.get(key),
						LogLevel.INFO);
			}
		}

		result.stopLoggerWriters();
	}
}
