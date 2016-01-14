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
		if (testSuite != null && threadSignal != null) {
			this.testSuite = testSuite;
			this.threadSignal = threadSignal;
		}
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		userName = config.getUserNames().get(config.getUserIndex());
		logger = Logger.get(LoggerName.User);
		timer = new Timer();
		random = new Random();
	}

	public void run() {
		String prepareTestScriptName = testSuite.getPrepareTestScript().getName();
		String clearupTestScriptName = testSuite.getClearupTestScript().getName();
		logger.write("TestThread - run() - Prepare test '" + prepareTestScriptName + "' start.", Level.DEBUG);
		execute(testSuite.getPrepareTestScript());
		logger.write("TestThread - run() - Prepare test '" + prepareTestScriptName + "' stopped.", Level.DEBUG);
		execute();
		logger.write("TestThread - run() - Clearup test '" + clearupTestScriptName + "' start.", Level.DEBUG);
		execute(testSuite.getClearupTestScript());
		logger.write("TestThread - run() - Clearup test '" + clearupTestScriptName + "' stopped.", Level.DEBUG);
		threadSignal.countDown();
	}

	private long execute(TestScript testScript) {
		long begin = System.currentTimeMillis();
		long duration = 0L;
		if (testScript != null) {
			String testScriptName = testScript.getName();
			logger.write("TestThread - execute(TestScript testScript) - Test '" + testScriptName + "' start.",
					Level.DEBUG);
			try {
				testScript.prepare();
				timer.begin();
				testScript.test();
				timer.end();
				testScript.clearup();
			} catch (Exception e) {
				result.addUserErrorCount(testScript.getName());
				StackTraceElement[] stackTrace = e.getStackTrace();
				StringBuilder message = new StringBuilder();
				for (StackTraceElement element : stackTrace) {
					message.append(element.toString()).append("\n");
				}
				logger.write("TestThread - execute(TestScript testScript) - Execute test '" + testScriptName
						+ "' failed. " + e.getMessage() + "\n" + message, Level.ERROR);
			}
			result.setTimerQueue(testScript.getName(), userName, String.valueOf(timer.getDuration()));
			logger.write("TestThread - execute(TestScript testScript) - Test '" + testScriptName + "' stopped.",
					Level.DEBUG);
			result.addUserExecutionCount(testScript.getName());
			duration = System.currentTimeMillis() - begin;
			logger.write("TestThread - execute(TestScript testScript) - Test '" + testScriptName + "' executed "
					+ duration + " ms.", Level.DEBUG);
		} else {
			logger.write("TestThread - execute(TestScript testScript) - Parameter testScript is null.", Level.ERROR);
		}
		return duration;
	}

	private void executeThinkTime(long testDuration) {
		long plannedThinkTime = config.getThinkTime();
		boolean randomThinkTime = config.isRandomThinkTime();
		long actualThinkTime = 0L;
		if (testDuration > 0) {
			if (testDuration < plannedThinkTime) {
				actualThinkTime = plannedThinkTime - testDuration;
			} else {
				logger.write(
						"TestThread - executeThinkTime(long testDuration) - Test case execution duration was longer than think time, actual think time set to zero.",
						Level.WARN);
				actualThinkTime = 0L;
			}
		} else {
			actualThinkTime = plannedThinkTime;
		}
		if (randomThinkTime) {
			if (actualThinkTime <= Integer.MAX_VALUE - 1) {
				actualThinkTime = random.nextInt((int) (actualThinkTime + 1));
			} else {
				actualThinkTime = random.nextInt((int) Integer.MAX_VALUE);
			}
		}
		logger.write("TestThread - executeThinkTime(long testDuration) - Sleep for " + actualThinkTime + " ms.",
				Level.DEBUG);
		try {
			Thread.sleep(actualThinkTime);
		} catch (InterruptedException e) {
			logger.write("TestThread - executeThinkTime(long testDuration) - Sleep interrupted abnormally.",
					Level.ERROR);
			e.printStackTrace();
		}
	}

	private void execute() {
		Iterator<TestCase> iterator = testSuite.getTestCaseList().iterator();
		long testDuration = 0L;
		// sequentially run
		int loop = config.getLoops();
		if (loop > 0) {
			logger.write("TestThread - execute() - Sequential run start.", Level.INFO);
			for (int i = 0; i < loop; i++) {
				while (iterator.hasNext()) {
					TestCase testcase = iterator.next();
					if (testcase != null) {
						testDuration = execute(testcase.getTestScript());
						executeThinkTime(testDuration);
					}
				}
			}
			logger.write("TestThread - execute() - Sequential run stopped.", Level.INFO);
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
			logger.write("TestThread - execute() - Random run preparation done with totalPercentage=" + totalPercentage,
					Level.DEBUG);
			if (config.getLogLevel() == Level.DEBUG) {
				logger.write("TestThread - execute() - Random run preparation done with testScriptPool", Level.DEBUG);
				for (int i = 0; i < testScriptPool.length; i++) {
					logger.write("Index=" + i + ": " + testScriptPool[i].getName(), Level.DEBUG);
				}
			}
			// random run
			logger.write("TestThread - execute() - Random run start.", Level.INFO);
			long wholeTestDuration = config.getWholeTestDuration() * 1000;
			long begin = System.currentTimeMillis();
			int testScriptPoolIndex = 0;
			while (System.currentTimeMillis() - begin < wholeTestDuration) {
				testScriptPoolIndex = random.nextInt(totalPercentage);
				testDuration = execute(testScriptPool[testScriptPoolIndex]);
				executeThinkTime(testDuration);
			}
			logger.write("TestThread - execute() - Random run stopped.", Level.INFO);
		}
	}
}
