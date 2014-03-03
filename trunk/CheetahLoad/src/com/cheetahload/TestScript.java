package com.cheetahload;

import java.util.Hashtable;

public abstract class TestScript {
	private String caseName;
	protected Hashtable caseParameters;

	public TestScript() {

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