package com.cheetahload;

import java.util.HashMap;

public abstract class TestScript {
	
	private String caseName = this.getClass().getSimpleName();
	protected HashMap<String, Object> caseParameters = new HashMap<String, Object>();

	public void setName(String caseName) {
		this.caseName = caseName;
	}

	public String getName() {
		return caseName;
	}

	public abstract void prepare() throws Exception;

	public abstract void test() throws Exception;

	public abstract void clearup() throws Exception;
}