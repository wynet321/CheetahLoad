package com.cheetahload.log;

import java.sql.Timestamp;
import java.util.HashMap;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public final class UserLogger extends Logger {
	
	private TestConfiguration config;
	private TestResult result;
	private static HashMap<String, UserLogger> userLoggerMap;

	public static UserLogger getUserLogger(String userName) {
		if (userLoggerMap == null) {
			userLoggerMap = new HashMap<String, UserLogger>();
		}
		if (!userLoggerMap.containsKey(userName)) {
			userLoggerMap.put(userName, new UserLogger(userName));
		}
		return userLoggerMap.get(userName);
	}

	private UserLogger(String userName) {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		this.userName = userName;
		this.path = config.getLogPath() + userName + ".log";
		this.level = config.getLogLevel();
	}

	@Override
	public void write(String message, Level lineLevel) {
		if (lineLevel.ordinal() <= level.ordinal()) {
			result.getUserLogBuffer(userName).append(
					new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " " + message + "\r");
		}
	}
}
