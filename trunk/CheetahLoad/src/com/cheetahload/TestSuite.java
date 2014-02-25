package com.cheetahload;

import java.util.ArrayList;

public final class TestSuite {
	private ArrayList<TestCase> testCaseList = new ArrayList<TestCase>();
	private TestScript prepareTestCase;
	private TestScript clearupTestCase;
	
	public final void add(TestCase testItem) {
		testCaseList.add(testItem);
	}

	public final ArrayList<TestCase> getTestItemList() {
		return testCaseList;
	}
	
	public final TestScript getPrepareTestCase(){
		return prepareTestCase;
	}
	
	public final TestScript getClearupTestCase(){
		return clearupTestCase;
	}
	
	public final void setPrepareTestCase(TestScript prepareTestCase){
		this.prepareTestCase=prepareTestCase;
	}
	
	public final void setClearupTestCase(TestScript clearupTestCase){
		this.clearupTestCase=clearupTestCase;
	}
}

