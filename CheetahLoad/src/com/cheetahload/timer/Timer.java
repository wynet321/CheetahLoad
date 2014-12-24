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
	}

	public long getBeginTime() {
		return begin;
	}

	public long getEndTime() {
		return end;
	}

	public long getDuration() {
		return duration;
	}

	public String getCurrentTime() {
		return new SimpleDateFormat("yyyy-MM-dd	HH:mm:ss SSS").format(System
				.currentTimeMillis());
	}
}
