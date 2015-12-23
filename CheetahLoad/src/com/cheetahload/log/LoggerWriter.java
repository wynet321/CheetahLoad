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
	
	public abstract void writeToFile();
	
	public void setStopSignal(boolean stopSignal){
		this.stopSignal = stopSignal;
	}

	public void run(){
		while (!stopSignal) {
			try {
				sleep(logWriteRate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writeToFile();
		}
		// write all of timer buffer to file
		writeToFile();
	}
}
