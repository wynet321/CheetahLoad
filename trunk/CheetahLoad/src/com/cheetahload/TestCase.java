package com.cheetahload;

public abstract class TestCase {
	private String caseName;
	public TestCase(){
		
	}
	public TestCase(String caseName){
		this.caseName=caseName;
	}
	public void setName(String caseName){
		this.caseName=caseName;
	}
	public String getName(){
		return caseName;
	}
	public abstract void prepare();
	public abstract void test();
	public abstract void clearup();
}
