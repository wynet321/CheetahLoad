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

	public TestResult() {
		userLogBufferTable = new Hashtable<String, StringBuffer>();
		userLogFileCountTable = new Hashtable<String, Integer>();
		userExecutionCountTable = new Hashtable<String, Integer>();
		userErrorCountTable = new Hashtable<String, Integer>();
		commonLogBuffer = new StringBuffer();
		timerQueue = new ConcurrentLinkedQueue<String>();
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
		// TODO judge parameter
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
		// TODO judge parameter
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
		// TODO judge parameter
		if (!userLogBufferTable.containsKey(userName)) {
			userLogBufferTable.put(userName, new StringBuffer());
		}
		return userLogBufferTable.get(userName);
	}

	public int getUserLogFileCount(String userName) {
		// TODO judge parameter
		if (!userLogFileCountTable.containsKey(userName)) {
			userLogFileCountTable.put(userName, 0);
		}
		return userLogFileCountTable.get(userName);
	}

	public void setUserLogFileCount(String userName, int logFileCount) {
		// TODO judge parameter
		userLogFileCountTable.put(userName, logFileCount);
	}

	public void setTimerQueue(String testName, String scriptName, String userName, String duration) {
		Logger logger = Logger.get(LoggerName.Common);
		if (testName == null || testName.isEmpty()) {
			logger.write("TestResult - setTimerQueue - testName was null or empty.", Level.ERROR);
			return;
		}
		if (scriptName == null || scriptName.isEmpty()) {
			logger.write("TestResult - setTimerQueue - scriptName was null or empty.", Level.ERROR);
			return;
		}
		if (userName == null || userName.isEmpty()) {
			logger.write("TestResult - setTimerQueue - userName was null or empty.", Level.ERROR);
			return;
		}
		if (duration == null || duration.isEmpty()) {
			logger.write("TestResult - setTimerQueue - duration was null or empty.", Level.ERROR);
			return;
		}
		StringBuilder item = new StringBuilder();
		item.append("'").append(testName).append("','").append(scriptName).append("','").append(userName).append("',")
				.append(duration);
		if (!timerQueue.add(item.toString())) {
			logger.write("TestResult - setTimerQueue - Add to queue failed. item: '" + item.toString() + "'",
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
