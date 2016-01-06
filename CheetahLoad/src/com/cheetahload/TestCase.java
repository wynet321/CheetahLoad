package com.cheetahload;


public final class TestCase{
	
	private TestScript testScript;
	private int percentage;
	
	public TestCase(TestScript testScript, int percentage){
		// TODO judge parameter
		this.testScript=testScript;
		this.percentage=percentage;
	}
	
	public final TestScript getTestScript(){
		return testScript;
	}
	
	public final int getPercentage(){
		return percentage;
	}
}
