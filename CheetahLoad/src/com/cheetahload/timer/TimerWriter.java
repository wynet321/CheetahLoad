package com.cheetahload.timer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestConfiguration;

public final class TimerWriter extends Thread {
	private boolean stopSignal;
	private StringBuffer buffer;
	private TestConfiguration config;

	public TimerWriter() {
		config = TestConfiguration.getTestConfiguration();
		stopSignal = false;
		buffer = new StringBuffer();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
		// write all of timer buffer left to file
		for (String key : config.getTimerQueueMap().keySet()) {
			ConcurrentLinkedQueue<String> queue = config.getTimerQueueMap().get(key);
			while (!queue.isEmpty()) {
				buffer.append(queue.poll());
			}
			write(key, buffer.toString());
			buffer.setLength(0);
		}
	}

	public void run() {
		while (!stopSignal) {
			for (String key : config.getTimerQueueMap().keySet()) {
				ConcurrentLinkedQueue<String> queue = config.getTimerQueueMap().get(key);
				while (queue.size() > 10240) {
					for (int i = 0; i < 10240; i++) {
						buffer.append(queue.poll());
					}
					write(key, buffer.toString());
					buffer.setLength(0);
				}

			}
			// every 10 secs to write timer log
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void write(String testScriptName, String content) {
		String path = config.getTimerLogPath() + "/" + testScriptName + ".log";
		File file = new File(path);
		FileWriter logWriter;
		try {
			if (file.exists()) {
				logWriter = new FileWriter(path, true);
			} else {
				logWriter = new FileWriter(path, false);
			}
			logWriter.write(content);
			logWriter.flush();
			logWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
