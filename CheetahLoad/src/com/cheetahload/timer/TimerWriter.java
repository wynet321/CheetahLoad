package com.cheetahload.timer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public final class TimerWriter extends Thread {

	private boolean stopSignal;
	private StringBuffer buffer;
	private TestConfiguration config;
	private TestResult result;
	private FileWriter logWriter;
	private int logToFileRate;

	public TimerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		stopSignal = false;
		buffer = new StringBuffer();
		logToFileRate = config.getLogToFileRate();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		while (!stopSignal) {
			try {
				sleep(logToFileRate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writeToFile();
		}
		// write all of timer buffer left to file
		writeToFile();
	}

	public void writeToFile() {
		for (String key : result.getTimerBufferKeySet()) {
			String path = config.getTimerLogPath() + "/" + key + ".log";
			File file = new File(path);
			buffer = result.getTimerBuffer(key);
			int bufferLength = buffer.length();
			try {
				if (file.exists()) {
					logWriter = new FileWriter(path, true);
				} else {
					logWriter = new FileWriter(path, false);
				}
				logWriter.write(buffer.substring(0, bufferLength));
				buffer.delete(0, bufferLength);
				logWriter.flush();
				logWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void write() {
		
		
	}

}
