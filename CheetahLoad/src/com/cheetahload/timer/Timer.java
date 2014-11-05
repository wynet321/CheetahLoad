package com.cheetahload.timer;

import java.text.SimpleDateFormat;

import com.cheetahload.TestConfiguration;
import com.cheetahload.executor.TestThread;

public final class Timer {
	private long duration = 0L;
	private long begin = 0L;
	private long end = 0L;

	public void begin() {
		begin = System.currentTimeMillis();
	}

	public void end() {
		end = System.currentTimeMillis();
		duration = end - begin;
		TestThread current = (TestThread) (Thread.currentThread());
		TestConfiguration.getTimerQueueMap().get(current.getName()).add(current.getUserName() + "," + duration + "," + begin + "," + end + "\n");
	}

	public String getCurrentTime() {
		return new SimpleDateFormat("yyyy-MM-dd	HH:mm:ss SSS").format(System.currentTimeMillis());
	}
}
