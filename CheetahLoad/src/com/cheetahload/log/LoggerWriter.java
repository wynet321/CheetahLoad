package com.cheetahload.log;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public abstract class LoggerWriter extends Thread {
	protected TestConfiguration config=TestConfiguration.getTestConfiguration();;
	protected TestResult result=TestResult.getTestResult();
	protected StringBuffer buffer=result.getCommonLogBuffer();
	protected int fileSize = config.getLogFileSize();
	protected int logWriteRate=config.getLogWriteRate();
	protected boolean stopSignal = false;
	protected int fileCount = 0;
	
	public abstract void setStopSignal(boolean stopSignal);

	public abstract void run();
}
