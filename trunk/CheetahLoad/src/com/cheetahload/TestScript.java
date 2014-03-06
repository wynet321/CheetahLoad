package com.cheetahload;

import java.util.Hashtable;

import com.cheetahload.executor.TestThread;
import com.cheetahload.log.Logger;

public abstract class TestScript {
	private String caseName;
	protected Hashtable caseParameters;

	public TestScript() {
		this.caseName = this.getClass().getSimpleName();
		caseParameters = new Hashtable();
	}

	public final Logger getLogger() {
		return ((TestThread) (Thread.currentThread())).getLogger();
	}

	public TestScript(String caseName) {
		this.caseName = caseName;
	}

	public void setName(String caseName) {
		this.caseName = caseName;
	}

	public String getName() {
		return caseName;
	}

	public abstract void prepare();

	public abstract void test();

	public abstract void clearup();
}