package com.cheetahload.log;

public abstract class Logger {

	protected LogLevel level;
	protected String path;
	protected String userName;

	public abstract void write(String message, LogLevel lineLevel);

}
