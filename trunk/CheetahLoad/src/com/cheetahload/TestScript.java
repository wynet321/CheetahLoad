package com.cheetahload;

public abstract class TestScript {
	private String caseName;
	public TestScript(){
		
	}
	public TestScript(String caseName){
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
