package com.cheetahload.log;

import java.sql.Timestamp;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.executor.TestThread;

public final class UserLogger extends Logger {

	// private TestConfiguration config;
	// private TestResult result;
	// private static HashMap<String, UserLogger> userLoggerMap;

	// public static UserLogger getUserLogger(String userName) {
	// if (userLoggerMap == null) {
	// userLoggerMap = new HashMap<String, UserLogger>();
	// }
	// if (!userLoggerMap.containsKey(userName)) {
	// userLoggerMap.put(userName, new UserLogger(userName));
	// }
	// return userLoggerMap.get(userName);
	// }

	// public UserLogger() {
	// config = TestConfiguration.getTestConfiguration();
	// result = TestResult.getTestResult();
	// this.userName = userName;
	// this.path = config.getLogPath() + userName + ".log";
	// this.level = config.getLogLevel();
	// }

	// @Override
	// public void write(String message, Level lineLevel) {
	// // TODO Auto-generated method stub
	// ((TestThread)Thread.currentThread()).getUserName()
	// }
	// public getLoggerByUser(String userName){
	// return userLoggerMap.get(userName);
	// }

	@Override
	public void write(String message, Level lineLevel) {
		if (lineLevel.ordinal() <= TestConfiguration.getTestConfiguration().getLogLevel().ordinal()) {
			TestResult
					.getTestResult()
					.getUserLogBuffer(((TestThread) Thread.currentThread()).getUserName())
					.append(new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " "
							+ message + "\r");
		}
	}
}
