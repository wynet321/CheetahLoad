package com.cheetahload.log;

public abstract class Logger {

	private static CommonLogger commonLogger;
	private static UserLogger userLogger;

	public abstract void write(String message, Level lineLevel);

	public static Logger get(LoggerName loggerName) {
		if (null == loggerName) {
			System.out.println("Logger - get() - Can't get null logger. Log system initial failed.");
			System.exit(0);
		} else {
			if (null == userLogger) {
				userLogger = new UserLogger();
			}
			if (null == commonLogger) {
				commonLogger = new CommonLogger();
			}

			if (loggerName == LoggerName.User) {
				return userLogger;
			} else if (loggerName == LoggerName.Common) {
				return commonLogger;
			}
		}
		return commonLogger;
	}
}
