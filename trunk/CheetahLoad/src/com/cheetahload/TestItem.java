package com.cheetahload;


public final class TestItem{
	
	private TestItem testItem;
	private TestCase testCase;
	private int percentage;
	
	public TestItem(TestCase testCase, int percentage)
	{
		this.testCase=testCase;
		this.percentage=percentage;
	}
	
	public final TestItem getTestItem(){
		return testItem;
	}
	
	public final TestCase getTestCase(){
		return testCase;
	}
	
	public final int getPercentage(){
		return percentage;
	}
}
