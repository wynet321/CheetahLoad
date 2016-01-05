package com.cheetahload.log;

import java.sql.Timestamp;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public final class CommonLogger extends Logger {
	@Override
	public void write(String message, Level lineLevel) {
		if (lineLevel.ordinal() <= TestConfiguration.getTestConfiguration().getLogLevel().ordinal()) {
			TestResult.getTestResult().getCommonLogBuffer().append(new Timestamp(System.currentTimeMillis()).toString()
					+ " " + lineLevel.toString() + " " + message + "\r");
		}
	}
}
