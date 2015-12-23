package com.cheetahload.timer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;
import com.cheetahload.utility.DB;

public final class TimerWriter extends Thread {

	private boolean stopSignal;
	// private Vector<String> timerVector;
	private TestConfiguration config;
	private TestResult result;
	// private FileWriter logWriter;

	private int logWriteRate;

	// private Connection connection;

	public TimerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		stopSignal = false;
		// timerVector = new Vector<String>();
		logWriteRate = config.getLogWriteRate();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		while (!stopSignal) {
			try {
				sleep(logWriteRate);
			} catch (InterruptedException e) {
				Logger.get(LoggerName.Common).write("TimerWriter - run() - sleep interrupted abnormally.", Level.ERROR);
				e.printStackTrace();
			}
			// writeToFile();
			writeToDB();
		}
		// write all of timer buffer left to file
		// writeToFile();
		writeToDB();
		DB.closeConnection();
	}

	public void writeToDB() {
		List<String> sql = new LinkedList<String>();
		ConcurrentLinkedQueue<String> timerQueue = result.getTimerQueue();
		String prefix = "insert into timer values(";
		String suffix = ")";
		while (!timerQueue.isEmpty()) {
			sql.add(prefix + timerQueue.poll() + suffix);
		}

		if (!DB.insert(sql)) {
			stopSignal = true;
			Logger.get(LoggerName.Common).write(
					"TimerWriter - writeToDB() - DB timer insert failed. Timer record thread stopped.", Level.ERROR);
		}
	}

	// public void writeToDB() {
	// if (!DB.insert("insert into timer values(?,?,?,?)",
	// result.getTimerQueue())) {
	// stopSignal = true;
	// Logger.get(LoggerName.Common).write(
	// "TimerWriter - writeToDB() - Insert into DB failed. Stop TimerWriter
	// thread.", Level.ERROR);
	// }
	// }
	// public void writeToFile() {
	// for (String key : result.getTimerBufferKeySet()) {
	// String path = config.getTimerLogPath() + "/" + key + ".log";
	// File file = new File(path);
	// list = result.getTimerList(key);
	// int bufferLength = list.length();
	// try {
	// if (file.exists()) {
	// logWriter = new FileWriter(path, true);
	// } else {
	// logWriter = new FileWriter(path, false);
	// }
	// logWriter.write(list.substring(0, bufferLength));
	// list.delete(0, bufferLength);
	// logWriter.flush();
	// logWriter.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
}
