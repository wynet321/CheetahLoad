package com.cheetahload.log;

import java.sql.Timestamp;
import java.util.HashMap;

import com.cheetahload.TestResult;

public final class UserLogger extends Logger {

	private TestResult result;
	private static HashMap<String, UserLogger> userLoggerMap;

	public static UserLogger getLogger(String userName) {
		if (userLoggerMap == null) {
			userLoggerMap = new HashMap<String, UserLogger>();
		}
		if (!userLoggerMap.containsKey(userName)) {
			userLoggerMap.put(userName, new UserLogger(userName));
		}
		return userLoggerMap.get(userName);
	}

	private UserLogger(String userName) {
		result = TestResult.getTestResult();
		this.userName = userName;
		this.path = result.getLogPath() + userName + ".log";
		this.level = result.getLogLevel();
	}

	@Override
	public void write(String message, LogLevel lineLevel) {
		if (lineLevel.ordinal() <= level.ordinal()) {
			result.getUserLogBuffer(userName).append(
					new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " " + message
							+ "\r");
		}
	}
}
