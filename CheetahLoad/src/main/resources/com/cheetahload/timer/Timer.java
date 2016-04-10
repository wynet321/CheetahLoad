package main.resources.com.cheetahload.timer;

import java.util.concurrent.TimeUnit;

public final class Timer {

	private long begin = 0L;
	private long end = 0L;

	public void begin() {
		begin = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
	}

	public void end() {
		end = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
	}

	public long getDuration() {
		long duration = end - begin;
		return (duration < 0) ? 0 : duration;
	}
}
