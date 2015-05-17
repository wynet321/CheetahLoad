package com.cheetahload.log;

public abstract class LoggerWriter extends Thread {
	protected boolean stopSignal = false;
	protected int fileCount = 0;
	protected int fileSize = 1024000;

	public abstract void stopWriter();

	public abstract void run();
}
