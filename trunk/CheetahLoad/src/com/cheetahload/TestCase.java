package com.cheetahload;


public final class TestCase{
	
	private TestCase testCase;
	private TestScript testScript;
	private int percentage;
	
	public TestCase(TestScript testCase, int percentage)
	{
		this.testScript=testCase;
		this.percentage=percentage;
	}
	
	public final TestCase getTestItem(){
		return testCase;
	}
	
	public final TestScript getTestCase(){
		return testScript;
	}
	
	public final int getPercentage(){
		return percentage;
	}
}
