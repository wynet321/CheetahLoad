package com.cheetahload;

import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;

public class TestResult {

	private Hashtable<String, Integer> userExecutionCountTable;
	private Hashtable<String, Integer> userErrorCountTable;
	private Hashtable<String, StringBuffer> userLogBufferTable;
	private static TestResult testResult;
	private Hashtable<String, Integer> userLogFileCountTable;
	private StringBuffer commonLogBuffer;
	private ConcurrentLinkedQueue<String> timerQueue;
	private Logger logger;

	public TestResult() {
		userLogBufferTable = new Hashtable<String, StringBuffer>();
		userLogFileCountTable = new Hashtable<String, Integer>();
		userExecutionCountTable = new Hashtable<String, Integer>();
		userErrorCountTable = new Hashtable<String, Integer>();
		commonLogBuffer = new StringBuffer();
		timerQueue = new ConcurrentLinkedQueue<String>();
		logger = Logger.get(LoggerName.Common);
	}

	public static TestResult getTestResult() {
		if (testResult == null) {
			testResult = new TestResult();
		}
		return testResult;
	}

	public Hashtable<String, Integer> getUserExecutionCountTable() {
		return userExecutionCountTable;
	}

	public synchronized void addUserExecutionCount(String testScriptName) {
		if (null == testScriptName || testScriptName.isEmpty()) {
			logger.write(
					"TestResult - addUserExecutionCount(String testScriptName) - testScriptName is null or empty string.",
					Level.ERROR);
			return;
		}

		if (userExecutionCountTable.containsKey(testScriptName)) {
			userExecutionCountTable.put(testScriptName, 1 + userExecutionCountTable.get(testScriptName));
		} else {
			userExecutionCountTable.put(testScriptName, 1);
		}
	}

	public Hashtable<String, Integer> getUserErrorCountTable() {
		return userErrorCountTable;
	}

	public synchronized void addUserErrorCount(String testScriptName) {
		if (null == testScriptName || testScriptName.isEmpty()) {
			logger.write("TestResult - addUserErrorCount() - testScriptName is null or empty string.", Level.ERROR);
			return;
		}

		if (userErrorCountTable.containsKey(testScriptName)) {
			userErrorCountTable.put(testScriptName, 1 + userErrorCountTable.get(testScriptName));
		} else {
			userErrorCountTable.put(testScriptName, 1);
		}
	}

	public Set<String> getUserLogBufferKeySet() {
		return userLogBufferTable.keySet();
	}

	public StringBuffer getUserLogBuffer(String userName) {
		if (null == userName || userName.isEmpty()) {
			logger.write("TestResult - getUserLogBuffer() - userName is null or empty string.", Level.ERROR);
			return new StringBuffer();
		}

		if (!userLogBufferTable.containsKey(userName)) {
			userLogBufferTable.put(userName, new StringBuffer());
		}
		return userLogBufferTable.get(userName);
	}

	public int getUserLogFileCount(String userName) {
		if (null == userName || userName.isEmpty()) {
			logger.write("TestResult - getUserLogFileCount() - userName is null or empty string.", Level.ERROR);
			return 0;
		}

		if (!userLogFileCountTable.containsKey(userName)) {
			userLogFileCountTable.put(userName, 0);
		}
		return userLogFileCountTable.get(userName);
	}

	public void setUserLogFileCount(String userName, int logFileCount) {
		if (null == userName || userName.isEmpty()) {
			logger.write("TestResult - setUserLogFileCount() - userName is null or empty string.", Level.ERROR);
			return;
		}
		if (logFileCount < 0) {
			userLogFileCountTable.put(userName, 0);
		}

		userLogFileCountTable.put(userName, logFileCount);
	}

	public void setTimerQueue(String scriptName, String userName, String duration) {
		if (scriptName == null || scriptName.isEmpty()) {
			logger.write("TestResult - setTimerQueue() - scriptName was null or empty.", Level.ERROR);
			return;
		}
		if (userName == null || userName.isEmpty()) {
			logger.write("TestResult - setTimerQueue() - userName was null or empty.", Level.ERROR);
			return;
		}
		if (duration == null || duration.isEmpty()) {
			logger.write("TestResult - setTimerQueue() - duration was null or empty.", Level.ERROR);
			return;
		}
		StringBuilder item = new StringBuilder();
		item.append("'").append(scriptName).append("','").append(userName).append("',").append(duration);
		if (!timerQueue.add(item.toString())) {
			logger.write("TestResult - setTimerQueue() - Add to queue failed. item: '" + item.toString() + "'",
					Level.ERROR);
		}
	}

	public ConcurrentLinkedQueue<String> getTimerQueue() {
		return timerQueue;
	}

	public StringBuffer getCommonLogBuffer() {
		return commonLogBuffer;
	}
}
