package com.cheetahload.log;

public abstract class Logger {

	private static CommonLogger commonLogger;
	private static UserLogger userLogger;

	public abstract void write(String message, Level lineLevel);

	public static Logger get(LoggerName loggerName) {
		if (loggerName == LoggerName.User) {
			if (null == userLogger) {
				userLogger = new UserLogger();
			}
			return userLogger;
		} else {
			if (null == commonLogger) {
				commonLogger = new CommonLogger();
			}
			return commonLogger;
		}
	}

}
