package com.cheetahload;

import java.io.File;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import com.cheetahload.log.CommonLoggerWriter;
import com.cheetahload.log.LogLevel;
import com.cheetahload.log.UserLoggerWriter;
import com.cheetahload.timer.TimerWriter;

public class TestResult {

	private Hashtable<String, Integer> userExecutionCountTable;
	private Hashtable<String, Integer> userErrorCountTable;
	private Hashtable<String, StringBuffer> userLogBufferTable;
	private static TestResult testResult;
	private Hashtable<String, Integer> userLogFileCountTable;
	private Hashtable<String, Vector<String>> timerListTable;
	private StringBuffer commonLogBuffer;
	private String logPath;
	private LogLevel logLevel;
	private int logFileSize;
	private int logToFileRate;
	private String resultPath;

	public int getLogToFileRate() {
		return logToFileRate;
	}

	public void setLogToFileRate(int logToFileRate) {
		this.logToFileRate = logToFileRate;
	}

	public boolean initializeResultFolder() {
		File dir = new File(resultPath);
		if (!dir.exists()) {
			// create result folder.
			if (dir.mkdirs()) {
				return true;
			} else {
				System.out.println("TestConfiguration - initializeResultPath() Create folder '" + resultPath
						+ "' failed. Please check permission.");
				return false;
			}
		}
		return true;
	}

	public boolean initializeLogFolder() {
		File dir = new File(logPath);
		if (dir.exists()) {
			if (dir.isDirectory()) {
				if (!clearDirectory(new File(logPath))) {
					System.out.println("TestConfiguration - initializeLogPath() Clear folder '" + logPath
							+ "' failed. Please clear by manual.");
					return false;
				}
			} else {
				if (!dir.delete()) {
					System.out.println("TestConfiguration - initializeLogPath() Delete file '" + logPath
							+ "' failed. Please delete by manual.");
					return false;
				}
				if (!dir.mkdir()) {
					System.out.println("TestConfiguration - initializeLogPath() Create folder '" + logPath
							+ "' failed. Please create by manual.");
					return false;
				}
			}
			return true;
		} else {
			if (dir.mkdirs()) {
				return true;
			} else {
				System.out.println("TestConfiguration - initializeLogPath() Create folder '" + logPath
						+ "' failed. Please check permission.");
				return false;
			}
		}
	}

	public void setLogLocation(String logLocation) {
		this.logPath = logLocation;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogPath() {
		return logPath;
	}

	private boolean clearDirectory(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = clearDirectory(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		} else {
			return dir.delete();
		}
		return true;
	}

	public String getResultPath() {
		return resultPath;
	}

	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}

	public int getLogFileSize() {
		return logFileSize;
	}

	public void setLogFileSize(int logFileSize) {
		this.logFileSize = logFileSize;
	}

	public void startLoggerWriters() {
		CommonLoggerWriter.getLoggerWriter().start();
		TimerWriter.getTimerWriter().start();
		UserLoggerWriter.getUserLoggerWriter().start();
	}

	public void stopLoggerWriters() {
		TimerWriter.getTimerWriter().stopWriter();
		UserLoggerWriter.getUserLoggerWriter().stopWriter();
		CommonLoggerWriter.getLoggerWriter().stopWriter();
	}

	public TestResult() {
		userLogBufferTable = new Hashtable<String, StringBuffer>();
		timerListTable = new Hashtable<String, Vector<String>>();
		userLogFileCountTable = new Hashtable<String, Integer>();
		userExecutionCountTable = new Hashtable<String, Integer>();
		userErrorCountTable = new Hashtable<String, Integer>();
		commonLogBuffer = new StringBuffer();
		resultPath = "./result";
		logPath = resultPath + "/log";
		logLevel = LogLevel.ERROR;
		logFileSize = 1024000;
		logToFileRate = 10000;

		if (!initializeResultFolder()) {
			System.out.println("TestResult - TestResult() Inintialize result folder failed. Path: " + resultPath);
			System.exit(0);
		}

		if (!initializeLogFolder()) {
			System.out.println("TestResult - TestResult() Inintialize result folder failed. Path: " + logPath);
			System.exit(0);
		}
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
