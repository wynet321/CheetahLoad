package com.cheetahload.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestConfiguration;

public final class UserLoggerWriter extends Thread {
	private boolean stopSignal = false;
	private StringBuffer buffer = new StringBuffer();
	private int fileCount = 0;
	private int fileSize = 1024000;

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		while (!stopSignal) {
			for (String key : TestConfiguration.getUserLoggerQueueMap().keySet()) {
				ConcurrentLinkedQueue<String> queue = TestConfiguration.getUserLoggerQueueMap().get(key);
				while (queue.size() > 10240) {
					buffer.setLength(0);
					for (int i = 0; i < 10240; i++) {
						buffer.append(queue.poll());
					}
					write(key);
				}
			}
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// write all of timer buffer to file
		for (String key : TestConfiguration.getUserLoggerQueueMap().keySet()) {
			ConcurrentLinkedQueue<String> queue = TestConfiguration.getUserLoggerQueueMap().get(key);
			buffer.setLength(0);
			while (!queue.isEmpty()) {
				buffer.append(queue.poll());
			}
			write(key);
		}
	}

	public void write(String userName) {
		String path = TestConfiguration.getLogPath() + "/" + userName + ".log";
		File file = new File(path);
		FileWriter logWriter;
		try {
			if (file.exists()) {
				logWriter = new FileWriter(path, true);
			} else {
				logWriter = new FileWriter(path, false);
			}
			while (buffer.length() >= fileSize) {
				logWriter.write(buffer.substring(0, fileSize));
				buffer.delete(0, fileSize);
				//buffer = buffer.substring(fileSize + 1);
				logWriter.flush();
				logWriter.close();
				file.renameTo(new File(path + "." + String.valueOf(++fileCount)));
				logWriter = new FileWriter(path, false);
			}
			if (stopSignal) {
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
