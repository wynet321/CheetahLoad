package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class CommonLoggerWriter extends LoggerWriter {

	public void writeToFile() {
		String path = config.getLogPath() + "/cheetahload.log";
		File file = new File(path);
		FileWriter logWriter;
		StringBuffer buffer = result.getCommonLogBuffer();
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
			System.out.println("UserLoggerWriter - writeToFile - Write to file '" + path + "' failed.");
			e.printStackTrace();
		}
	}
}
