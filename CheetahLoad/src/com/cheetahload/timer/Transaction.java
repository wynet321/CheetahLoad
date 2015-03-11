package com.cheetahload.timer;

import com.cheetahload.TestConfiguration;
import com.cheetahload.executor.TestThread;

public final class Transaction {
	private String name;
	private long begin = 0L;
	private long end = 0L;
	private long duration = 0L;

	public Transaction(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	public void begin() {
		begin = System.currentTimeMillis();
	}

	public void end() {
		end = System.currentTimeMillis();
		duration = end - begin;
		TestThread current = (TestThread) (Thread.currentThread());
		TestConfiguration.getTimerQueueMap().get(current.getName()).add(current.getUserName() + "," + duration + "," + begin + "," + end + "\n");
	}
}
