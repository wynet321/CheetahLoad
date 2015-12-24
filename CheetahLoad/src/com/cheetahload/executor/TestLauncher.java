package com.cheetahload.executor;

import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.TestSuite;
import com.cheetahload.db.Configuration;
import com.cheetahload.db.DB;
import com.cheetahload.log.CommonLoggerWriter;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;
import com.cheetahload.log.LoggerWriter;
import com.cheetahload.log.UserLoggerWriter;
import com.cheetahload.timer.TimerWriter;

public final class TestLauncher {

	private static LoggerWriter commonLoggerWriter;
	private static LoggerWriter userLoggerWriter;
	private static TimerWriter timerWriter;

	private static void startLogger() {
		commonLoggerWriter = new CommonLoggerWriter();
		commonLoggerWriter.start();

		userLoggerWriter = new UserLoggerWriter();
		userLoggerWriter.start();

		timerWriter = new TimerWriter();
		timerWriter.start();
	}

	public static void stopLogger() {
		timerWriter.setStopSignal(true);
		userLoggerWriter.setStopSignal(true);
		commonLoggerWriter.setStopSignal(true);
		while (timerWriter.isAlive() || userLoggerWriter.isAlive() || commonLoggerWriter.isAlive()) {
			try {
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

		if (config.verify()) {
			Configuration configEntry = new Configuration();
			configEntry.setDuration(config.getDuration());
			configEntry.setLogFileSize(config.getLogFileSize());
			configEntry.setLogLevel(config.getLogLevel());
			configEntry.setLogWriteRate(config.getLogWriteRate());
			configEntry.setLoops(config.getLoops());
			configEntry.setTesterName(config.getTestName());
			configEntry.setTesterMail(config.getTesterMail());
			configEntry.setTestName(config.getTestName());
			configEntry.setTestSuiteName(config.getTestSuiteName());
			configEntry.setThinkTime(config.getThinkTime());
			configEntry.setUserCount(config.getUserCount());
			if (!DB.insert(configEntry)) {
				Logger.get(LoggerName.Common).write(
						"TestLauncher - run() Test configuration parameters failed to insert into DB. Test will continue to run...",
						Level.ERROR);
			}

		} else {
			Logger.get(LoggerName.Common).write(
					"TestLauncher - run() Test configuration settings are not completed. Test can't start! ",
					Level.ERROR);
			System.exit(0);
		}

		startLogger();
		System.out.println("Test is starting...");
		// Thread(VU) start
		int threadCount = config.getUserCount();
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
			Logger.get(LoggerName.Common).write(
					"TestLauncher - run() Thread can't be started. Error: " + e.getStackTrace().toString(),
					Level.ERROR);
		}

		// output statistic data
		Logger.get(LoggerName.Common).write("TestLauncher - run() Execution Summary Start.", Level.INFO);
		Hashtable<String, Integer> userExecutionCountTable = result.getUserExecutionCountTable();
		for (String key : userExecutionCountTable.keySet()) {
			Logger.get(LoggerName.Common).write(
					"TestEntry - runTest() " + key + " execution count: " + userExecutionCountTable.get(key),
					Level.INFO);
		}

		Hashtable<String, Integer> userErrorCountTable = result.getUserErrorCountTable();
		if (userErrorCountTable.isEmpty()) {
			Logger.get(LoggerName.Common).write("TestLauncher - run() There is no error in test.", Level.INFO);
		} else {
			for (String key : userErrorCountTable.keySet()) {
				Logger.get(LoggerName.Common).write(
						"TestLauncher - run() " + key + " error count: " + userErrorCountTable.get(key), Level.INFO);
			}
		}

		Logger.get(LoggerName.Common).write("TestLauncher - run() Execution Summary End.", Level.INFO);
		stopLogger();
	}
}
