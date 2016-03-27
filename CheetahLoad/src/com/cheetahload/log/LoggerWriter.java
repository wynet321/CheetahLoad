package com.cheetahload.log;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public abstract class LoggerWriter extends Thread {
	protected TestConfiguration config;
	protected TestResult result;
	protected StringBuffer buffer;
	protected int fileSize;
	protected int logWriteCycle;
	protected boolean stopSignal;
	protected int fileCount;

	public LoggerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		buffer = result.getCommonLogBuffer();
		fileSize = config.getLogFileSize();
		logWriteCycle = config.getLogWriteCycleTime();
		stopSignal = false;
		fileCount = 0;
	}

	public abstract void write();

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		while (!stopSignal) {
			try {
				sleep(logWriteCycle);
			} catch (InterruptedException e) {
				System.out.println("LoggerWriter - run() - Sleep interrupted abnormally.");
				e.printStackTrace();
			}
			write();
		}
		// write the left of timer buffer to file
		write();
	}
}
