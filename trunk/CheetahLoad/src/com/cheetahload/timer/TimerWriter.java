package com.cheetahload.timer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestConfiguration;

public final class TimerWriter extends Thread {
	private boolean stopSignal = false;
	private StringBuffer buffer = new StringBuffer();

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		while (!stopSignal) {
			for (String key : TestConfiguration.getTimerQueueMap().keySet()) {
				ConcurrentLinkedQueue<String> queue = TestConfiguration.getTimerQueueMap().get(key);
				while (queue.size() > 10240) {
					for (int i = 0; i < 10240; i++) {
						buffer.append(queue.poll());
					}
					write(key, buffer.toString());
					buffer.setLength(0);
				}

			}
			try {
				sleep(300000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// write all of timer buffer to file
		for (String key : TestConfiguration.getTimerQueueMap().keySet()) {
			ConcurrentLinkedQueue<String> queue = TestConfiguration.getTimerQueueMap().get(key);
			while (!queue.isEmpty()) {
				buffer.append(queue.poll());
			}
			write(key, buffer.toString());
			buffer.setLength(0);
		}

	}

	public void write(String testScriptName, String content) {
		String path = TestConfiguration.getTimerLogPath() + "/" + testScriptName + ".log";
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
