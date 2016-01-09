package com.cheetahload.timer;

import com.cheetahload.TestResult;
import com.cheetahload.executor.TestThread;

public final class Transaction {

	private String name;
	private long begin = 0L;
	private long end = 0L;
	private long duration = 0L;
	private TestResult result;

	public Transaction(String name) {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		} else {
			this.name = "Transaction_" + System.currentTimeMillis();
		}
		result = TestResult.getTestResult();
	}

	public String getName() {
		return name;
	}

	public void begin() {
		begin = System.currentTimeMillis();
	}

	public void end() {
		end = System.currentTimeMillis();
		duration = end - begin;
		TestThread current = (TestThread) (Thread.currentThread());
		// TODO add transaction to db
		// result.getTimerVector(current.getName()).add(current.getUserName() +
		// "," + duration + "," + begin + "," + end + "\n");
		// result.setTimerQueue(current.getName(), , userName, duration);
		result.setTimerQueue(config.getTestName(), testScript.getName(), userName,
				String.valueOf(timer.getDuration()));
	}
}
