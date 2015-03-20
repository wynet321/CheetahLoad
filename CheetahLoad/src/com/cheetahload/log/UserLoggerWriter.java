package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestConfiguration;

public final class UserLoggerWriter extends LoggerWriter {
	private StringBuffer buffer;
	private ConcurrentLinkedQueue<String> queue;
	private TestConfiguration config;

	public UserLoggerWriter() {
		config = TestConfiguration.getTestConfiguration();
		buffer = new StringBuffer();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
		// write all of timer buffer to file
		for (String key : config.getUserLoggerQueueMap().keySet()) {
			queue = config.getUserLoggerQueueMap().get(key);
			while (!queue.isEmpty()) {
				buffer.append(queue.poll());
			}
			write(key);
		}
	}

	public void run() {
		while (!stopSignal) {
			for (String key : config.getUserLoggerQueueMap().keySet()) {
				queue = config.getUserLoggerQueueMap().get(key);
				while (queue.size() > 10240) {
					for (int i = 0; i < 10240; i++) {
						buffer.append(queue.poll());
					}
					write(key);
				}
			}
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void write(String userName) {
		String path = config.getLogPath() + "/" + userName + ".log";
		File file = new File(path);
		FileWriter logWriter;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
