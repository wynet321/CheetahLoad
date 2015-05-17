package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cheetahload.TestResult;

public final class UserLoggerWriter extends LoggerWriter {

	private StringBuffer buffer;
	private TestResult result;
	private FileWriter logWriter;
	private static UserLoggerWriter userLoggerWriter;
	private int logToFileRate;

	public static UserLoggerWriter getUserLoggerWriter() {
		if (userLoggerWriter == null)
			userLoggerWriter = new UserLoggerWriter();
		return userLoggerWriter;
	}

	private UserLoggerWriter() {
		result = TestResult.getTestResult();
		fileSize = result.getLogFileSize();
		logToFileRate = result.getLogToFileRate();
	}

	@Override
	public void stopWriter() {
		stopSignal = true;
		writeToFile();
	}

	public void run() {
		while (!stopSignal) {
			writeToFile();
			try {
				sleep(logToFileRate);
			} catch (InterruptedException e) {
				CommonLogger.getLogger().write("UserLoggerWriter - run() Thread sleep failed. " + e.getMessage(),
						LogLevel.ERROR);
			}
		}
		// write left timer buffer to file
		writeToFile();
	}

	public void writeToFile() {
		for (String key : result.getUserLogBufferKeySet()) {
			String path = result.getLogPath() + "/" + key + ".log";
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
				result.setUserLogFileCount(key, fileCount);

				if (stopSignal) {
					logWriter = new FileWriter(path, false);
					logWriter.write(buffer.toString());
					logWriter.flush();
					logWriter.close();
					this.interrupt();
				}
			} catch (IOException e) {
				CommonLogger.getLogger().write(
						"UserLoggerWriter - writeToFile() Failed to write user log files. Path: " + path + ". "
								+ e.getMessage(), LogLevel.ERROR);
			}
		}
	}

}
