package com.cheetahload;


public final class TestCase{
	
	private TestScript testScript;
	private int percentage;
	
	public TestCase(TestScript testCase, int percentage)
	{
		this.testScript=testCase;
		this.percentage=percentage;
	}
	
	public final TestScript getTestScript(){
		return testScript;
	}
	
	public final int getPercentage(){
		return percentage;
	}
}
