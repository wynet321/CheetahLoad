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

	public UserLoggerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		// buffer = new StringBuffer();
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
			for (String key : result.getUserLogBufferMap().keySet()) {
				write(key);
			}
		}
		// write all of timer buffer to file
		for (String key : result.getUserLogBufferMap().keySet()) {
			write(key);
		}
	}

	public void write(String userName) {
		String path = config.getLogPath() + "/" + userName + ".log";
		File file = new File(path);
		FileWriter logWriter;
		buffer = result.getUserLogBufferMap().get(userName);
		fileCount = result.getUserLogFileCount().get(userName);
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
			result.getUserLogFileCount().put(userName, fileCount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
