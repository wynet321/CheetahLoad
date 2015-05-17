package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cheetahload.TestResult;

public final class CommonLoggerWriter extends LoggerWriter {

	private StringBuffer buffer;
	private TestResult result;
	private int logToFileRate;
	private static CommonLoggerWriter commonLoggerWriter;

	public static CommonLoggerWriter getLoggerWriter() {
		if (commonLoggerWriter == null)
			commonLoggerWriter = new CommonLoggerWriter();
		return commonLoggerWriter;
	}

	private CommonLoggerWriter() {
		result = TestResult.getTestResult();
		logToFileRate = result.getLogToFileRate();
	}

	public void stopWriter() {
		stopSignal = true;
		writeToFile();
	}

	public void run() {
		while (!stopSignal) {
			// while (true) {
			writeToFile();
			try {
				sleep(logToFileRate);
			} catch (InterruptedException e) {
				CommonLogger.getLogger().write(
						"CommonLoggerWriter - run() Failed to sleep CommonLoggerWriter thread. " + e.getMessage(),
						LogLevel.ERROR);
			}
		}
		writeToFile();
	}

	public void writeToFile() {
		String path = result.getLogPath() + "/cheetahload.log";
		File file = new File(path);
		FileWriter logWriter;
		buffer = result.getCommonLogBuffer();
		try {
			while (buffer.length() >= fileSize) {
				logWriter = new FileWriter(path, false);
				logWriter.write(buffer.substring(0, fileSize));
				buffer.delete(0, fileSize);
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
		} catch (IOException e) {
			CommonLogger.getLogger().write(
					"CommonLoggerWriter - writeToFile() Failed to write to common log file. Path: " + path + ". "
							+ e.getMessage(), LogLevel.ERROR);
		}
	}
}
