package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public final class UserLoggerWriter extends LoggerWriter {

	private StringBuffer buffer;
	private TestConfiguration config;
	private TestResult result;
	private FileWriter logWriter;

	public UserLoggerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		fileSize = config.getLogFileSize();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		while (!stopSignal) {
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writeToFile();
		}
		// write all of timer buffer to file
		writeToFile();
	}

	public void writeToFile() {
		for (String key : result.getUserLogBufferKeySet()) {
			String path = config.getLogPath() + "/" + key + ".log";
			File file = new File(path);
			buffer = result.getUserLogBuffer(key);
			fileCount = result.getUserLogFileCount(key);
			try {
				while (buffer.length() >= fileSize) {
					logWriter = new FileWriter(path, false);
					int i = 0;
					while (buffer.charAt(fileSize - i) != 13) {
						i++;
					}
					logWriter.write(buffer.substring(0, fileSize - i + 1));
					buffer.delete(0, fileSize - i + 1);
					logWriter.flush();
					logWriter.close();
					file.renameTo(new File(path + "." + String.valueOf(++fileCount)));
				}
				if (stopSignal) {
					logWriter = new FileWriter(path, false);
					logWriter.write(buffer.toString());
					logWriter.flush();
					logWriter.close();
				}
				result.setUserLogFileCount(key, fileCount);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
