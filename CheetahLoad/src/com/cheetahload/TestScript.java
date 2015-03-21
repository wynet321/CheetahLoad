package com.cheetahload;

import java.util.HashMap;

import com.cheetahload.executor.TestThread;
import com.cheetahload.log.UserLogger;

public abstract class TestScript {
	private String caseName = this.getClass().getSimpleName();
	protected HashMap<String, Object> caseParameters = new HashMap<String, Object>();

	public final UserLogger getUserLogger() {
		return ((TestThread) (Thread.currentThread())).getUserLogger();
	}

	// public TestScript(String caseName) {
	// this.caseName = caseName;
	// }

	public void setName(String caseName) {
		this.caseName = caseName;
	}

	public String getName() {
		return caseName;
	}

	public void runTest() {
		TestThread currentThread = (TestThread) (Thread.currentThread());
		currentThread.getTimer().begin();
		test();
		currentThread.getTimer().end();
	}

	public abstract void prepare();

	public abstract void test();

	public abstract void clearup();
}