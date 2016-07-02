package main.resources.com.cheetahload.log;

import java.sql.Timestamp;

import main.resources.com.cheetahload.TestConfiguration;
import main.resources.com.cheetahload.TestResult;

public final class CommonLogger extends Logger {

	@Override
	public void add(String message, Level lineLevel) {
		if (message != null && !message.isEmpty()) {
			if (lineLevel.ordinal() <= TestConfiguration.getTestConfiguration().getLogLevel().ordinal()) {
				TestResult
						.getTestResult()
						.getCommonLogBuffer()
						.append(new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " "
								+ message + "\r");
			}
		}
	}
}
