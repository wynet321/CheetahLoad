package com.cheetahload.timer;

import java.text.SimpleDateFormat;

import com.cheetahload.TestConfiguration;

public final class Timer {
	private long duration = 0L;
	private long begin = 0L;
	private long end = 0L;

	public void begin() {
		begin = System.currentTimeMillis();
	}

	public void end(String testScriptName, String userName) {
		end = System.currentTimeMillis();
		duration = end - begin;
		input(testScriptName, userName);
	}

	public String getCurrentTime() {
		return new SimpleDateFormat("yyyy-MM-dd	HH:mm:ss SSS").format(System.currentTimeMillis());
	}

	private void input(String testScriptName, String userName) {
		TestConfiguration.getTimerQueueMap().get(testScriptName)
				.add(userName + "," + duration + "," + begin + "," + end + "\n");
	}

}
