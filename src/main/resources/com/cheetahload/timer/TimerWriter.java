package main.resources.com.cheetahload.timer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import main.resources.com.cheetahload.TestConfiguration;
import main.resources.com.cheetahload.TestResult;
import main.resources.com.cheetahload.log.Level;
import main.resources.com.cheetahload.log.Logger;
import main.resources.com.cheetahload.log.LoggerName;

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
			write(result.getTimerQueue(), "insert into timer(script_name,vuser_name,duration) values(", ")");
			write(result.getTransactionTimerQueue(), "insert into tranx values(", ")");
		}
		// write the left of timer buffer left to DB
		write(result.getTimerQueue(), "insert into timer(script_name,vuser_name,duration) values(", ")");
		write(result.getTransactionTimerQueue(), "insert into tranx values(", ")");
	}

	public void write(ConcurrentLinkedQueue<String> queue, String sqlPrefix, String sqlSuffix) {
		if (queue != null) {
			List<String> sql = new LinkedList<String>();
			while (!queue.isEmpty()) {
				sql.add(sqlPrefix + queue.poll() + sqlSuffix);
			}
			if (!sql.isEmpty()) {
				if (!config.getOperator().execute(sql)) {
					stopSignal = true;
					if (sqlPrefix.contains("tranx")) {
						Logger.get(LoggerName.Common).add(
								"TimerWriter - write() - DB transaction timer insert failed. The whole timer record thread stopped.",
								Level.ERROR);
					} else {
						Logger.get(LoggerName.Common).add(
								"TimerWriter - write() - DB timer insert failed. The whole timer record thread stopped.",
								Level.ERROR);
					}
				}
			}
		}
	}
}
