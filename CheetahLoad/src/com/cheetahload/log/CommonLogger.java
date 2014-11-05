package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import com.cheetahload.TestConfiguration;

public final class CommonLogger extends Logger {
	private String text = new String();
	private int fileSize = 1024000;
	private FileWriter logWriter;
	private int fileCount = 0;
	
	public CommonLogger() {
		this.path = TestConfiguration.getLogPath() + "/cheetahload.log";
		this.level = TestConfiguration.getLogLevel();
	}
	
	@Override
	public void write(String message, Level lineLevel) {
		text += new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " " + message
				+ "\r";
		if (text.length() >= fileSize)
			flush(false);
	}

	public void flush(boolean flushAll) {
		File file = new File(path);
		try {
			if (file.exists()) {
				logWriter = new FileWriter(path, true);
			} else {
				logWriter = new FileWriter(path, false);
			}
			while (text.length() >= fileSize) {
				logWriter.write(text.substring(0, fileSize));
				logWriter.flush();
				logWriter.close();
				text = text.substring(fileSize + 1);
				file.renameTo(new File(path + "." + String.valueOf(++fileCount)));
				logWriter = new FileWriter(path, false);
			}
			if (flushAll) {
				logWriter.write(text);
				logWriter.flush();
				logWriter.close();
			}
		} catch (IOException e) {
			// TODO deal with IO exception

		}
	}
}
