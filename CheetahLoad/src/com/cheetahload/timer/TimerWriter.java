package com.cheetahload.timer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;

public final class TimerWriter extends Thread {

	private boolean stopSignal;
	private TestConfiguration config;
	private TestResult result;
	private int logWriteRate;

	public TimerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		stopSignal = false;
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
			writeToDB();
		}
		// write all of timer buffer left to DB
		writeToDB();
	}

	public void writeToDB() {
		List<String> sql = new LinkedList<String>();
		ConcurrentLinkedQueue<String> timerQueue = result.getTimerQueue();
		String prefix = "insert into timer values(";
		String suffix = ")";
		while (!timerQueue.isEmpty()) {
			sql.add(prefix + timerQueue.poll() + suffix);
		}
		if (!sql.isEmpty()) {
			if (!config.getOperator().insert(sql)) {
				stopSignal = true;
				Logger.get(LoggerName.Common)
						.write("TimerWriter - writeToDB() - DB timer insert failed. Timer record thread stopped.",
								Level.ERROR);
			}
		}
	}
}
