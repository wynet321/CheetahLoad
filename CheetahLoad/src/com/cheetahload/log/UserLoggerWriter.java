package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public final class UserLoggerWriter extends LoggerWriter {

	private StringBuffer buffer;
	private TestConfiguration config;
	private TestResult result;

	public UserLoggerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		fileSize = config.getLogFileSize();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		Set<String> userLogBufferKeySet = result.getUserLogBufferKeySet();
		while (!stopSignal) {
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String key : userLogBufferKeySet) {
				write(key);
			}
		}
		// write all of timer buffer to file
		for (String key : userLogBufferKeySet) {
			write(key);
		}
	}

	public void write(String userName) {
		String path = config.getLogPath() + "/" + userName + ".log";
		File file = new File(path);
		FileWriter logWriter;
		buffer = result.getUserLogBuffer(userName);
		fileCount = result.getUserLogFileCount(userName);
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
			result.setUserLogFileCount(userName, fileCount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
