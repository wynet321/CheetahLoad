package com.cheetahload;

import java.util.ArrayList;

public final class TestSuite {
	private ArrayList<TestItem> testItemList = new ArrayList<TestItem>();
	private TestCase prepareTestCase;
	private TestCase clearupTestCase;
	
	public final void add(TestItem testItem) {
		testItemList.add(testItem);
	}

	public final ArrayList<TestItem> getTestItemList() {
		return testItemList;
	}
	
	public final TestCase getPrepareTestCase(){
		return prepareTestCase;
	}
	
	public final TestCase getClearupTestCase(){
		return clearupTestCase;
	}
	
	public final void setPrepareTestCase(TestCase prepareTestCase){
		this.prepareTestCase=prepareTestCase;
	}
	
	public final void setClearupTestCase(TestCase clearupTestCase){
		this.clearupTestCase=clearupTestCase;
	}
}

