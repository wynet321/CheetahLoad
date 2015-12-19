package com.cheetahload.log;

import java.sql.Timestamp;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public final class CommonLogger extends Logger {
	
	private TestConfiguration config;
	private TestResult result;
	private static CommonLogger commonLogger;

	public static CommonLogger getCommonLogger() {
		if (commonLogger == null) {
			commonLogger = new CommonLogger();
		}
		return commonLogger;
	}

	private CommonLogger() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		this.path = config.getLogPath() + "/cheetahload.log";
		this.level = config.getLogLevel();
	}

	@Override
	public void write(String message, Level lineLevel) {
		result.getCommonLogBuffer().append(new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " " + message + "\r");
	}
}
