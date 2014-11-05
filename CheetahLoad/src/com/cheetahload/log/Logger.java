package com.cheetahload.log;

public abstract class Logger {

	protected Level level;
	protected String path;
	protected String userName;

	public abstract void write(String message, Level lineLevel);

}
