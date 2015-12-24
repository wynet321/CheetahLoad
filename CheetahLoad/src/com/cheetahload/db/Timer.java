package com.cheetahload.db;

public class Timer extends Entity {
	private String testName, scriptName, userName;
	private int duration;

	public Timer() {
		tableDef = "create table timer (testname varchar(50), scriptname varchar(20), username varchar(20),duration int)";
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
