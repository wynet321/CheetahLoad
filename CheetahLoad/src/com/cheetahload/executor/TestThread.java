package com.cheetahload.executor;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.cheetahload.TestCase;
import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.TestScript;
import com.cheetahload.TestSuite;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;
import com.cheetahload.timer.Timer;

public final class TestThread extends Thread {
	private TestSuite testSuite;
	// private UserLogger userLogger;
	private Logger logger;
	private String userName;
	private Timer timer;
	private Random random;
	private CountDownLatch threadSignal;
	private TestConfiguration config;
	private TestResult result;

	public String getUserName() {
		return userName;
	}

	public TestThread(TestSuite testSuite, CountDownLatch threadSignal) {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		this.testSuite = testSuite;
		this.threadSignal = threadSignal;
		userName = config.getUserNames().get(config.getUserIndex());
		// userLogger = UserLogger.getUserLogger(userName);
		logger = Logger.get(LoggerName.User);
		timer = new Timer();
		random = new Random();
	}

	// public UserLogger getUserLogger() {
	// return userLogger;
	// }

	public void run() {
		logger.write("TestThread - run() Prepare test " + testSuite.getPrepareTestScript().getName() + " start.",
				Level.DEBUG);
		execute(testSuite.getPrepareTestScript());
		logger.write("TestThread - run() Prepare test " + testSuite.getPrepareTestScript().getName() + " stopped.",
				Level.DEBUG);
		execute();
		logger.write("TestThread - run() Clearup test " + testSuite.getPrepareTestScript().getName() + " start.",
				Level.DEBUG);
		execute(testSuite.getClearupTestScript());
		logger.write("TestThread - run() Clearup test " + testSuite.getPrepareTestScript().getName() + " stopped.",
				Level.DEBUG);
		threadSignal.countDown();
	}

	private void execute(TestScript testScript) {
		if (testScript != null) {
			logger.write("TestThread - execute(TestScript) " + testScript.getName() + " start.", Level.DEBUG);
			try {
				testScript.prepare();
				timer.begin();
				testScript.test();
				timer.end();
				testScript.clearup();
			} catch (Exception e) {
				result.addUserErrorCount(testScript.getName());
				logger.write("TestThread - execute - Execute test case '" + testScript.getName() + "' failed. "
						+ e.getMessage(), Level.ERROR);
			}
			result.setTimerQueue(config.getTestName(), testScript.getName(), userName,
					String.valueOf(timer.getDuration()));
			logger.write("TestThread - execute(TestScript) " + testScript.getName() + " end.", Level.DEBUG);
			result.addUserExecutionCount(testScript.getName());
		}
	}

	private void execute() {
		Iterator<TestCase> iterator = testSuite.getTestCaseList().iterator();
		// sequentially run
		int loop = config.getLoops();
		if (loop > 0) {
			logger.write("TestThread - execute() sequential run start.", Level.INFO);
			for (int i = 0; i < loop; i++) {
				while (iterator.hasNext()) {
					TestCase testcase = iterator.next();
					if (testcase != null) {
						execute(testcase.getTestScript());
					}
				}
			}
			logger.write("TestThread - execute() sequential run stop.", Level.INFO);
		} else {
			// prepare random run script pool
			int totalPercentage = testSuite.getTotalPercentage();
			int percentageAccumulator = 0;
			TestCase testcase;
			TestScript[] testScriptPool = new TestScript[totalPercentage];
			while (iterator.hasNext()) {
				testcase = iterator.next();
				int percentage = testcase.getPercentage();
				for (int i = 0; i < percentage; i++) {
					testScriptPool[percentageAccumulator + i] = testcase.getTestScript();
				}
				percentageAccumulator += percentage;
			}
			logger.write("TestThread - execute() random run preparation done with totalPercentage=" + totalPercentage,
					Level.DEBUG);
			if (config.getLogLevel() == Level.DEBUG) {
				logger.write("TestThread - execute() random run preparation done with testScriptPool", Level.DEBUG);
				for (int i = 0; i < testScriptPool.length; i++) {
					logger.write("Index=" + i + ": " + testScriptPool[i].getName(), Level.DEBUG);
				}
			}
			// random run
			logger.write("TestThread - execute() random run start.", Level.INFO);
			long duration = config.getDuration() * 1000;
			long begin = System.currentTimeMillis();
			int testScriptPoolIndex = 0;
			while (System.currentTimeMillis() - begin < duration) {
				testScriptPoolIndex = random.nextInt(totalPercentage);
				execute(testScriptPool[testScriptPoolIndex]);
			}
			logger.write("TestThread - execute() random run stop.", Level.INFO);
		}
	}
}
