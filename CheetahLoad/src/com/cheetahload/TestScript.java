package com.cheetahload;

public abstract class TestScript {
	private String caseName;
	// protected HashMap<String, Object> caseParameters;

	public TestScript() {
		caseName = this.getClass().getSimpleName();
		// caseParameters = new HashMap<String, Object>();
	}

	public void setName(String caseName) {
		if (caseName != null) {
			this.caseName = caseName;
		}
	}

	public String getName() {
		return caseName;
	}

	public abstract void prepare() throws Exception;

	public abstract void test() throws Exception;

	public abstract void clearup() throws Exception;
}