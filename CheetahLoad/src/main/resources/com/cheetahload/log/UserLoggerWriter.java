package main.resources.com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class UserLoggerWriter extends LoggerWriter {
	public void write() {
		for (String key : result.getUserLogBufferKeySet()) {
			String path = config.getLogPath() + "/" + key + ".log";
			File file = new File(path);
			FileWriter logWriter;
			StringBuffer buffer = result.getUserLogBuffer(key);
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
				if (stopSignal) {
					logWriter = new FileWriter(path, false);
					logWriter.write(buffer.toString());
					logWriter.flush();
					logWriter.close();
				}
				result.setUserLogFileCount(key, fileCount);
			} catch (IOException e) {
				System.out.println("UserLoggerWriter - writeToFile - Write to file '" + path + "' failed.");
				e.printStackTrace();
			}
		}
	}
}
