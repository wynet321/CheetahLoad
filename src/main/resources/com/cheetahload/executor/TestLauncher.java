package main.resources.com.cheetahload.executor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import main.resources.com.cheetahload.TestConfiguration;
import main.resources.com.cheetahload.TestResult;
import main.resources.com.cheetahload.TestSuite;
import main.resources.com.cheetahload.db.JDBCOperator;
import main.resources.com.cheetahload.db.TableDefinition;
import main.resources.com.cheetahload.log.CommonLoggerWriter;
import main.resources.com.cheetahload.log.Level;
import main.resources.com.cheetahload.log.Logger;
import main.resources.com.cheetahload.log.LoggerName;
import main.resources.com.cheetahload.log.LoggerWriter;
import main.resources.com.cheetahload.log.UserLoggerWriter;
import main.resources.com.cheetahload.timer.TimerWriter;

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
		List<String> sql = new ArrayList<String>();
		JDBCOperator operator=TestConfiguration.getTestConfiguration().getOperator();
		for (TableDefinition set : TableDefinition.values()) {
			if(!operator.exists(set.name())){
				sql.add(set.getSql());
			}
		}
		if (!operator.execute(sql)) {
			Logger.get(LoggerName.Common).add(
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
					.append(config.getLogWriteCycleTime()).append(",'")
					.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())).append("',")
					.append("'')");
			if (!config.getOperator().execute(sql.toString())) {
				logger.add(
						"TestLauncher - run() - Test configuration parameters failed to insert into DB. Test will continue to run...",
						Level.ERROR);
			}
			logger.add("TestLauncher - run() - Test configuration parameters succeeded to insert into DB.",
					Level.DEBUG);
		} else {
			logger.add("TestLauncher - run() - Test configuration settings are not completed. Test can't start! ",
					Level.ERROR);
			System.exit(0);
		}

		if (testSuite != null) {
			System.out.println("Test is starting...");
			startLogger();
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
				logger.add("TestLauncher - run() - Thread can't be started. Error: " + e.getMessage(), Level.ERROR);
				e.printStackTrace();
			}

			// output statistic data
			Logger.get(LoggerName.Common).add("TestLauncher - run() - Execution Summary Start.", Level.INFO);
			Hashtable<String, Integer> userExecutionCountTable = result.getUserExecutionCountTable();
			for (String key : userExecutionCountTable.keySet()) {
				logger.add("TestEntry - runTest() - " + key + " execution count: " + userExecutionCountTable.get(key),
						Level.INFO);
			}

			Hashtable<String, Integer> userErrorCountTable = result.getUserErrorCountTable();
			if (userErrorCountTable.isEmpty()) {
				logger.add("TestLauncher - run() - There is no error in test.", Level.INFO);
			} else {
				for (String key : userErrorCountTable.keySet()) {
					logger.add("TestLauncher - run() - " + key + " error count: " + userErrorCountTable.get(key),
							Level.INFO);
				}
			}
			logger.add("TestLauncher - run() - Execution Summary End.", Level.INFO);

			StringBuilder sql = new StringBuilder();
			sql.append("update configuration set end_time='")
					.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()))
					.append("' where test_name='").append(config.getTestName()).append("'");
			if (!config.getOperator().execute(sql.toString())) {
				logger.add("TestLauncher - run() - Test end time failed to update to DB.", Level.ERROR);
			}
			stopLogger();
			System.out.println("Test is completed.");
		}
	}
}
