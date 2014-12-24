package com.cheetahload.executor;

import java.util.ArrayList;
import java.util.Iterator;

import com.cheetahload.TestCase;
import com.cheetahload.TestConfiguration;
import com.cheetahload.TestScript;
import com.cheetahload.TestSuite;
import com.cheetahload.log.Level;
import com.cheetahload.log.UserLogger;
import com.cheetahload.timer.Timer;

public final class TestThread extends Thread {
	TestSuite testSuite;
	private UserLogger userLogger;
	private String userName;
	private Timer timer;

	public String getUserName() {
		return userName;
	}

	public Timer getTimer() {
		return timer;
	}

	public TestThread(TestSuite testSuite) {
		this.testSuite = testSuite;
		userName = TestConfiguration.getUserNames().get(
				TestConfiguration.getUserIndex());
		userLogger = new UserLogger(userName);
		timer = new Timer();
	}

	public UserLogger getUserLogger() {
		return userLogger;
	}

	public void run() {
		execute(testSuite.getPrepareTestCase());
		execute(testSuite.getTestCaseList());
		execute(testSuite.getClearupTestCase());
	}

	private void execute(TestScript testScript) {
		if (testScript != null) {
			testScript.prepare();
			timer.begin();
			testScript.test();
			timer.end();
			testScript.clearup();
			TestConfiguration
					.getTimerQueueMap()
					.get(testScript.getName())
					.add(userName + "," + timer.getDuration() + ","
							+ timer.getBeginTime() + "," + timer.getEndTime()
							+ "\n");
		}
	}

	private void execute(ArrayList<TestCase> testItemList) {
		// loop
		// TODO deal with percentage
		Iterator<TestCase> iterator = testSuite.getTestCaseList().iterator();
		while (iterator.hasNext()) {
			TestCase testcase = iterator.next();
			if (testcase != null) {
				// System.out.println(this.getName() +
				// testcase.getTestScript().getName());
				userLogger.write("TestThread - execute(ArrayList<TestCase>) "
						+ testcase.getTestScript().getName() + " start.",
						Level.DEBUG);
				execute(testcase.getTestScript());
				userLogger.write("TestThread - execute(ArrayList<TestCase>) "
						+ testcase.getTestScript().getName() + " end.",
						Level.DEBUG);
			}

		}
	}
}
