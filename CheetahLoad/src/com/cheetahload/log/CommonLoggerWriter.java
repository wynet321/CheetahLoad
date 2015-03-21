package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestConfiguration;

public final class CommonLoggerWriter extends LoggerWriter {
	private ConcurrentLinkedQueue<String> queue;
	private StringBuffer buffer;
	private TestConfiguration config;

	public CommonLoggerWriter() {
		config = TestConfiguration.getTestConfiguration();
		queue = config.getCommonLogQueue();
		buffer = new StringBuffer();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;

	}

	public void run() {
		while (!stopSignal) {
			if (queue.size() >= fileSize) {
				for (int i = 0; i < 10240; i++) {
					buffer.append(queue.poll());
				}
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
		while (!queue.isEmpty()) {
			buffer.append(queue.poll());
		}
		write();
	}

	public void write() {
		String path = config.getLogPath() + "/cheetahload.log";
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
			// TODO deal with IO exception
			e.printStackTrace();
		}
	}
}
