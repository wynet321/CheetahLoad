package com.cheetahload.log;

import java.sql.Timestamp;

import com.cheetahload.TestConfiguration;

public final class UserLogger extends Logger{
	
	public UserLogger(String userName) {
		this.userName = userName;
		this.path = TestConfiguration.getLogPath() + userName + ".log";
		this.level = TestConfiguration.getLogLevel();
	}

	@Override
	public void write(String message, Level lineLevel) {
		if (lineLevel.ordinal() <= level.ordinal()) {
			TestConfiguration
					.getUserLoggerQueueMap()
					.get(userName)
					.add(new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " "
							+ message + "\r");
		}
	}
}
