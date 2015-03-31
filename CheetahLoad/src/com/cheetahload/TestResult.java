package com.cheetahload;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

public class TestResult {

	private HashMap<String, Integer> userExecutionCountMap;
	private HashMap<String, Integer> userErrorCountMap;
	private HashMap<String, StringBuffer> userLogBufferMap;
	private static TestResult testResult;
	private HashMap<String, Integer> userLogFileCountMap;
	private Hashtable<String, Vector<String>> timerListTable;
	private StringBuffer commonLogBuffer;

	public TestResult() {
		userLogBufferMap = new HashMap<String, StringBuffer>();
		timerListTable = new Hashtable<String, Vector<String>>();
		userLogFileCountMap = new HashMap<String, Integer>();
		userExecutionCountMap = new HashMap<String, Integer>();
		userErrorCountMap = new HashMap<String, Integer>();
		commonLogBuffer = new StringBuffer();

	}

	public static TestResult getTestResult() {
		if (testResult == null) {
			testResult = new TestResult();
		}
		return testResult;
	}

	public HashMap<String, Integer> getUserExecutionCountMap() {
		return userExecutionCountMap;
	}

	public synchronized void addUserExecutionCount(String testScriptName) {
		if (userExecutionCountMap.containsKey(testScriptName)) {
			userExecutionCountMap.put(testScriptName, 1 + userExecutionCountMap.get(testScriptName));
		} else {
			userExecutionCountMap.put(testScriptName, 1);
		}
	}

	public HashMap<String, Integer> getUserErrorCountMap() {
		return userErrorCountMap;
	}

	public synchronized void addUserErrorCount(String testScriptName) {
		if (userErrorCountMap.containsKey(testScriptName)) {
			userErrorCountMap.put(testScriptName, 1 + userErrorCountMap.get(testScriptName));
		} else {
			userErrorCountMap.put(testScriptName, 1);
		}
	}

	public Set<String> getUserLogBufferKeySet() {
		return userLogBufferMap.keySet();
	}

	public StringBuffer getUserLogBuffer(String userName) {
		if (!userLogBufferMap.containsKey(userName)) {
			userLogBufferMap.put(userName, new StringBuffer());
		}
		return userLogBufferMap.get(userName);
	}

	public int getUserLogFileCount(String userName) {
		if (!userLogFileCountMap.containsKey(userName)) {
			userLogFileCountMap.put(userName, 0);
		}
		return userLogFileCountMap.get(userName);
	}

	public void setUserLogFileCount(String userName, int logFileCount) {
		userLogFileCountMap.put(userName, logFileCount);
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
