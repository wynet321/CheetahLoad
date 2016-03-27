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
	private int logWriteCycle;

	public TimerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		stopSignal = false;
		logWriteCycle = config.getLogWriteCycleTime();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		while (!stopSignal) {
			try {
				sleep(logWriteCycle);
			} catch (InterruptedException e) {
				Logger.get(LoggerName.Common).add("TimerWriter - run() - Sleep interrupted abnormally.", Level.ERROR);
				e.printStackTrace();
			}
			write();
		}
		// write the left of timer buffer left to DB
		write();
	}

	public void write() {
		List<String> sql = new LinkedList<String>();
		ConcurrentLinkedQueue<String> timerQueue = result.getTimerQueue();
		String prefix = "insert into timer(script_name,vuser_name,duration) values(";
		String suffix = ")";
		while (!timerQueue.isEmpty()) {
			sql.add(prefix + timerQueue.poll() + suffix);
		}
		if (!sql.isEmpty()) {
			if (!config.getOperator().execute(sql)) {
				stopSignal = true;
				Logger.get(LoggerName.Common).add(
						"TimerWriter - writeToDB() - DB timer insert failed. Timer record thread stopped.",
						Level.ERROR);
			}
		}
	}
}
