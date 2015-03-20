package com.cheetahload.log;

import java.sql.Timestamp;

import com.cheetahload.TestConfiguration;

public final class UserLogger extends Logger {
	private TestConfiguration config;

	public UserLogger(String userName) {
		config = TestConfiguration.getTestConfiguration();
		this.userName = userName;
		this.path = config.getLogPath() + userName + ".log";
		this.level = config.getLogLevel();
	}

	@Override
	public void write(String message, Level lineLevel) {
		if (lineLevel.ordinal() <= level.ordinal()) {
			config.getUserLoggerQueueMap().get(userName)
					.add(new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " " + message + "\r");
		}
	}
}
