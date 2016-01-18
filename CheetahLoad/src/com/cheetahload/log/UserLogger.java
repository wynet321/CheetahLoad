package com.cheetahload.log;

import java.sql.Timestamp;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.executor.TestThread;

public final class UserLogger extends Logger {
	@Override
	public void add(String message, Level lineLevel) {
		if (message != null && !message.isEmpty()) {
			if (lineLevel.ordinal() <= TestConfiguration.getTestConfiguration().getLogLevel().ordinal()) {
				TestResult
						.getTestResult()
						.getUserLogBuffer(((TestThread) Thread.currentThread()).getUserName())
						.append(new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " "
								+ message + "\r");
			}
		}
	}
}
