package com.cheetahload;

import com.cheetahload.executor.TestThread;

public abstract class TestScript {
	private String caseName;

	public TestScript() {
		//get default value of caseName;
		caseName = this.getClass().getSimpleName();
	}

	public void setName(String caseName) {
		if (caseName != null) {
			this.caseName = caseName;
		}
	}

	public String getName() {
		return caseName;
	}

	//Do not recommend use parameter method to deliver object between scripts.
	protected Object getParameter(String key) {
		return ((TestThread) Thread.currentThread()).getDataMapValue(key);
	}

	//Do not recommend use parameter method to deliver object between scripts.
	protected void setParameter(String key, Object object) {
		((TestThread) Thread.currentThread()).setDataMapValue(key, object);
	}

	public abstract void prepare() throws Exception;

	public abstract void test() throws Exception;

	public abstract void clearup() throws Exception;
}