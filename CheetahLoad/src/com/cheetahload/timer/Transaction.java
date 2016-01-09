package com.cheetahload.timer;

import com.cheetahload.TestConfiguration;
import com.cheetahload.executor.TestThread;

public final class Transaction {

	private String name;
	private long begin = 0L;
	private long end = 0L;
	private long duration = 0L;
	private String scriptName, vuName;

	public Transaction(String transactionName) {
		if (transactionName == null || transactionName.isEmpty()) {
			this.name = "Transaction_" + System.currentTimeMillis();
		} else {
			this.name = transactionName;
		}
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
		scriptName = current.getName();
		vuName = current.getUserName();
		String sql = "insert into transaction values(" + scriptName + "','" + vuName + "','" + name + "'," + duration
				+ ")";
		TestConfiguration.getTestConfiguration().getOperator().insert(sql);
	}
}
