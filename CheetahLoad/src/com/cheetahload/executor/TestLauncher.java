package com.cheetahload.executor;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.TestSuite;
import com.cheetahload.db.TableDefinition;
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
				System.out.println("TestLauncher - stopLogger() - Sleep failed.");
				e.printStackTrace();
			}
		}
	}

	private static void createTables() {
		List<String> sql = new LinkedList<String>();
		sql.add(TableDefinition.CONFIGURATION);
		sql.add(TableDefinition.TIMER);
		sql.add(TableDefinition.TRANSACTION);
		if (!TestConfiguration.getTestConfiguration().getOperator().insert(sql)) {
			Logger.get(LoggerName.Common).write(
					"TestLauncher - createTables() - Test configuration parameters failed to insert into DB. Test will continue to run without time record...",
					Level.ERROR);
		}
	}

	public final static void start(TestSuite testSuite) {
		TestResult result = TestResult.getTestResult();
		TestConfiguration config = TestConfiguration.getTestConfiguration();
		Logger logger = Logger.get(LoggerName.Common);
		if (config.verify()) {
			createTables();
			StringBuilder sql = new StringBuilder();
			sql.append("insert into configuration values('").append(config.getTestName()).append("','")
					.append(config.getTesterName()).append("','").append(config.getTesterMail()).append("','")
					.append(config.getTestSuiteName()).append("',").append(config.getUserCount()).append(",")
					.append(config.getWholeTestDuration()).append(",").append(config.getLoops()).append(",")
					.append(config.getThinkTime()).append(",'").append(config.isRandomThinkTime()).append("','")
					.append(config.getLogLevel()).append("',").append(config.getLogFileSize()).append(",")
					.append(config.getLogWriteRate()).append(")");
			if (!config.getOperator().insert(sql.toString())) {
				logger.write(
						"TestLauncher - run() - Test configuration parameters failed to insert into DB. Test will continue to run...",
						Level.ERROR);
			}
			logger.write("TestLauncher - run() - Test configuration parameters succeeded to insert into DB.",
					Level.DEBUG);
		} else {
			logger.write("TestLauncher - run() - Test configuration settings are not completed. Test can't start! ",
					Level.ERROR);
			System.exit(0);
		}

		startLogger();
		if (testSuite != null) {
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
				logger.write("TestLauncher - run() - Thread can't be started. Error: " + e.getMessage(), Level.ERROR);
				e.printStackTrace();
			}

			// output statistic data
			Logger.get(LoggerName.Common).write("TestLauncher - run() - Execution Summary Start.", Level.INFO);
			Hashtable<String, Integer> userExecutionCountTable = result.getUserExecutionCountTable();
			for (String key : userExecutionCountTable.keySet()) {
				logger.write("TestEntry - runTest() - " + key + " execution count: " + userExecutionCountTable.get(key),
						Level.INFO);
			}

			Hashtable<String, Integer> userErrorCountTable = result.getUserErrorCountTable();
			if (userErrorCountTable.isEmpty()) {
				logger.write("TestLauncher - run() - There is no error in test.", Level.INFO);
			} else {
				for (String key : userErrorCountTable.keySet()) {
					logger.write("TestLauncher - run() - " + key + " error count: " + userErrorCountTable.get(key),
							Level.INFO);
				}
			}

			logger.write("TestLauncher - run() - Execution Summary End.", Level.INFO);
			stopLogger();
			System.out.println("Test is completed.");
		}
	}
}
