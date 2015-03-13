package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import com.cheetahload.TestConfiguration;

public final class CommonLogger extends Logger {
	private String content = new String();
	private int fileSize = 1024000;
	private FileWriter logWriter;
	private int fileCount = 0;
	
	public CommonLogger() {
		this.path = TestConfiguration.getLogPath() + "/cheetahload.log";
		this.level = TestConfiguration.getLogLevel();
	}
	
	public String getCommonLoggerContent(){
		return content;
	}
	
	@Override
	public void write(String message, Level lineLevel) {
		content += new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " " + message
				+ "\r";
	}

	public void flush(boolean flushAll) {
		File file = new File(path);
		try {
			if (file.exists()) {
				logWriter = new FileWriter(path, true);
			} else {
				logWriter = new FileWriter(path, false);
			}
			while (content.length() >= fileSize) {
				logWriter.write(content.substring(0, fileSize));
				logWriter.flush();
				logWriter.close();
				content = content.substring(fileSize + 1);
				file.renameTo(new File(path + "." + String.valueOf(++fileCount)));
				logWriter = new FileWriter(path, false);
			}
			if (flushAll) {
				logWriter.write(content);
				logWriter.flush();
				logWriter.close();
			}
		} catch (IOException e) {
			// TODO deal with IO exception

		}
	}
}
