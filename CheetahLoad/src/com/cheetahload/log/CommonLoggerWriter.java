package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public final class CommonLoggerWriter extends LoggerWriter {

	private StringBuffer buffer;
	private TestConfiguration config;
	private TestResult result;

	public CommonLoggerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
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
			write();
		}
		write();
	}

	public void write() {
		String path = config.getLogPath() + "/cheetahload.log";
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
			// TODO deal with IO exception
			e.printStackTrace();
		}
	}
}
