package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cheetahload.TestConfiguration;

public final class CommonLoggerWriter extends LoggerWriter {
	private String commonLoggerContent;

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		while (!stopSignal) {
			commonLoggerContent += TestConfiguration.getCommonLogger().getCommonLoggerContent();
			if (commonLoggerContent.length() >= fileSize) {
				write();
			}
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// write left common log to file
		commonLoggerContent += TestConfiguration.getCommonLogger().getCommonLoggerContent();
		write();
	}

	public void write() {
		String path = TestConfiguration.getLogPath() + "/cheetahload.log";
		File file = new File(path);
		FileWriter logWriter;
		try {
			while (commonLoggerContent.length() >= fileSize) {
				logWriter = new FileWriter(path, false);
				logWriter.write(commonLoggerContent.substring(0, fileSize));
				logWriter.flush();
				logWriter.close();
				commonLoggerContent = commonLoggerContent.substring(fileSize + 1);
				file.renameTo(new File(path + "." + String.valueOf(++fileCount)));
			}
			if (stopSignal) {
				logWriter = new FileWriter(path, false);
				logWriter.write(commonLoggerContent);
				logWriter.flush();
				logWriter.close();
			}
		} catch (IOException e) {
			// TODO deal with IO exception
			e.printStackTrace();
		}
	}
}
