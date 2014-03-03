package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import com.cheetahload.TestConfiguration;

public class Logger {
	private FileWriter logWriter;
	private int fileCount;
	private int fileSize;
	private String path;
	private String text;
	private int textWriteUnitSize;
	private int textWriteUnitCount;
	private Level level;

	public Logger(String path, Level level) {
		fileSize = 1024000;
		textWriteUnitSize = 1024;
		textWriteUnitCount = 0;
		this.path = path;
		this.level = level;
		fileCount = 0;
	}

	public void write(String message, Level lineLevel) {
		if (lineLevel.ordinal() >= level.ordinal()) {
			text += new Timestamp(System.currentTimeMillis()).toString() + " " + lineLevel.toString() + " " + message
					+ "\r";
			if (text.length() >= fileSize)
				flush(false);
		}
	}

	public void flush(boolean flushAll) {
		File file = new File(path);
		try {
			if (file.exists()) {
				logWriter = new FileWriter(path, true);
			} else {
				logWriter = new FileWriter(path, false);
			}
			while (text.length() >= textWriteUnitSize) {
				logWriter.write(text.substring(0, textWriteUnitSize));
				logWriter.flush();
				text = text.substring(textWriteUnitSize, text.length() - textWriteUnitSize);
				textWriteUnitCount++;
				if (textWriteUnitCount >= fileSize / textWriteUnitSize) {
					logWriter.close();
					// generateNewLogFile
					file.renameTo(new File(path + String.valueOf(fileCount++)));
					textWriteUnitCount = 0;
					logWriter = new FileWriter(path, false);
				}
			}
			if (flushAll)
				logWriter.write(text);
			logWriter.close();
		} catch (IOException e) {
			// TODO deal with IO exception

		}
	}
}
