package com.cheetahload.timer;

import java.text.SimpleDateFormat;

import com.cheetahload.TestResult;

public final class Timer {
	private long duration = 0L;
	private long begin = 0L;
	private long end = 0L;
	private SimpleDateFormat timeFormat;
	private TestResult result;

	public Timer() {
		timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		result=TestResult.getTestResult();
	}

	public void begin() {
		begin = System.currentTimeMillis();
	}

	public void end() {
		end = System.currentTimeMillis();
		duration = end - begin;
	}

	public String getBeginTime() {
		return timeFormat.format(begin);
	}

	public String getEndTime() {
		return timeFormat.format(end);
	}

	public long getDuration() {
		return duration;
	}
	
	public void write(String scriptName,String message){
		result.getTimerVector(scriptName).add(message);
	}
}
