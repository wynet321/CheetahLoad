package com.cheetahload.timer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public final class TimerWriter extends Thread {
	private boolean stopSignal;
	private StringBuffer buffer;
	private TestConfiguration config;
	private TestResult result;

	// private ConcurrentLinkedQueue<String> queue;

	public TimerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		stopSignal = false;
		buffer = new StringBuffer();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;

	}

	public void run() {
		while (!stopSignal) {
			// every 10 secs to write timer log
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String key : result.getTimerBufferMap().keySet()) {
				// queue = config.getTimerQueueMap().get(key);
				// while (queue.size() > 10240) {
				// for (int i = 0; i < 10240; i++) {
				// buffer.append(queue.poll());
				// }
				write(key);
				// buffer.setLength(0);
				// }

			}
		}
		// write all of timer buffer left to file
		for (String key : result.getTimerBufferMap().keySet()) {
			// queue = config.getTimerQueueMap().get(key);
			// while (queue.size() > 10240) {
			// for (int i = 0; i < 10240; i++) {
			// buffer.append(queue.poll());
			// }
			write(key);
			// buffer.setLength(0);
			// }

		}
	}

	public void write(String testScriptName) {
		String path = config.getTimerLogPath() + "/" + testScriptName + ".log";
		File file = new File(path);
		FileWriter logWriter;
		buffer = result.getTimerBufferMap().get(testScriptName);
		int bufferLength = buffer.length();
		try {
			if (file.exists()) {
				logWriter = new FileWriter(path, true);
			} else {
				logWriter = new FileWriter(path, false);
			}
			logWriter.write(buffer.substring(0, bufferLength));
			buffer.delete(0, bufferLength);
			logWriter.flush();
			logWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
