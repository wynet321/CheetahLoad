package com.cheetahload.log;

import java.sql.Timestamp;

import com.cheetahload.TestResult;

public final class CommonLogger extends Logger {

	private TestResult result;
	private static CommonLogger commonLogger;

	public static CommonLogger getLogger() {
		if (commonLogger == null) {
			commonLogger = new CommonLogger();
		}
		return commonLogger;
	}

	private CommonLogger() {
		result = TestResult.getTestResult();
		this.path = result.getLogPath() + "/cheetahload.log";
		this.level = result.getLogLevel();
	}

	@Override
	public void write(String message, LogLevel lineLevel) {
		result.getCommonLogBuffer().append(
				new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " " + message
						+ "\r");
	}
}
