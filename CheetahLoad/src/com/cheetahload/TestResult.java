package com.cheetahload;

import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

public class TestResult {

	private Hashtable<String, Integer> userExecutionCountTable;
	private Hashtable<String, Integer> userErrorCountTable;
	private Hashtable<String, StringBuffer> userLogBufferTable;
	private static TestResult testResult;
	private Hashtable<String, Integer> userLogFileCountTable;
	private Hashtable<String, Vector<String>> timerListTable;
	private StringBuffer commonLogBuffer;

	public TestResult() {
		userLogBufferTable = new Hashtable<String, StringBuffer>();
		timerListTable = new Hashtable<String, Vector<String>>();
		userLogFileCountTable = new Hashtable<String, Integer>();
		userExecutionCountTable = new Hashtable<String, Integer>();
		userErrorCountTable = new Hashtable<String, Integer>();
		commonLogBuffer = new StringBuffer();

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
			userExecutionCountTable.put(testScriptName,
					1 + userExecutionCountTable.get(testScriptName));
		} else {
			userExecutionCountTable.put(testScriptName, 1);
		}
	}

	public Hashtable<String, Integer> getUserErrorCountTable() {
		return userErrorCountTable;
	}

	public synchronized void addUserErrorCount(String testScriptName) {
		if (userErrorCountTable.containsKey(testScriptName)) {
			userErrorCountTable.put(testScriptName,
					1 + userErrorCountTable.get(testScriptName));
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

	public Set<String> getTimerBufferKeySet() {
		return timerListTable.keySet();
	}

	public Vector<String> getTimerVector(String scriptName) {
		if (!timerListTable.containsKey(scriptName)) {
			timerListTable.put(scriptName, new Vector<String>());
		}
		return timerListTable.get(scriptName);
	}

	public StringBuffer getCommonLogBuffer() {
		return commonLogBuffer;
	}
}
