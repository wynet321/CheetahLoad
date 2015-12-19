package com.cheetahload;

import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.Level;

public class TestResult {

	private Hashtable<String, Integer> userExecutionCountTable;
	private Hashtable<String, Integer> userErrorCountTable;
	private Hashtable<String, StringBuffer> userLogBufferTable;
	private static TestResult testResult;
	private Hashtable<String, Integer> userLogFileCountTable;
	// private Hashtable<String, Vector<String>> timerListTable;
	private StringBuffer commonLogBuffer;
	private ConcurrentLinkedQueue<String> timerQueue;

	public TestResult() {
		userLogBufferTable = new Hashtable<String, StringBuffer>();
		// timerListTable = new Hashtable<String, Vector<String>>();
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
		if (!userLogBufferTable.containsKey(userName)) {
			userLogBufferTable.put(userName, new StringBuffer());
		}
		return userLogBufferTable.get(userName);
	}

	public int getUserLogFileCount(String userName) {
		if (!userLogFileCountTable.containsKey(userName)) {
			userLogFileCountTable.put(userName, 0);
		}
		return userLogFileCountTable.get(userName);
	}

	public void setUserLogFileCount(String userName, int logFileCount) {
		userLogFileCountTable.put(userName, logFileCount);
	}

	// public Set<String> getTimerBufferKeySet() {
	// return timerListTable.keySet();
	// }

	// public Vector<String> getTimerVector(String scriptName) {
	// if (!timerListTable.containsKey(scriptName)) {
	// timerListTable.put(scriptName, new Vector<String>());
	// }
	// return timerListTable.get(scriptName);
	// }
	//
	// public void setTimerVector(String testName, String scriptName, String
	// duration, String userName) {
	// if (!timerListTable.containsKey(scriptName)) {
	// timerListTable.put(scriptName, new Vector<String>());
	// }
	// timerListTable.get(scriptName).add(testName + "," + scriptName + "," +
	// duration + "," + userName);
	// }

	public void setTimerQueue(String testName, String scriptName, String userName, String duration) {
		if (testName == null || testName.isEmpty()) {
			CommonLogger.getCommonLogger().write("TestResult - setTimerQueue - testName was null or empty.",
					Level.ERROR);
			return;
		}
		if (scriptName == null || scriptName.isEmpty()) {
			CommonLogger.getCommonLogger().write("TestResult - setTimerQueue - scriptName was null or empty.",
					Level.ERROR);
			return;
		}
		if (userName == null || userName.isEmpty()) {
			CommonLogger.getCommonLogger().write("TestResult - setTimerQueue - userName was null or empty.",
					Level.ERROR);
			return;
		}
		if (duration == null || duration.isEmpty()) {
			CommonLogger.getCommonLogger().write("TestResult - setTimerQueue - duration was null or empty.",
					Level.ERROR);
			return;
		}
		StringBuilder item = new StringBuilder();
		item.append(testName).append(",").append(scriptName).append(",").append(userName).append(",").append(duration);
		if (!timerQueue.add(item.toString())) {
			CommonLogger.getCommonLogger().write(
					"TestResult - setTimerQueue - Add to queue failed. item: '" + item.toString() + "'", Level.ERROR);
		}
	}

	public ConcurrentLinkedQueue<String> getTimerQueue() {
		return timerQueue;
	}

	public StringBuffer getCommonLogBuffer() {
		return commonLogBuffer;
	}
}
