package com.cheetahload;

import java.util.ArrayList;

public final class TestSuite {
	private ArrayList<TestCase> testCaseList = new ArrayList<TestCase>();
	private int totalPercentage = 0;
	private TestScript prepareTestScript;
	private TestScript clearupTestScript;

	public final void add(TestCase testcase) {
		testCaseList.add(testcase);
		totalPercentage += testcase.getPercentage();
	}

	public final int getTotalPercentage() {
		return totalPercentage;
	}

	public final ArrayList<TestCase> getTestCaseList() {
		return testCaseList;
	}

	public final TestScript getPrepareTestScript() {
		return prepareTestScript;
	}

	public final TestScript getClearupTestScript() {
		return clearupTestScript;
	}

	public final void setPrepareTestScript(TestScript prepareTestScript) {
		this.prepareTestScript = prepareTestScript;
	}

	public final void setClearupTestScript(TestScript clearupTestScript) {
		this.clearupTestScript = clearupTestScript;
	}
}
