package com.cheetahload.db;

import com.cheetahload.log.Level;

public class Configuration extends Entity {
	private int duration;
	private int loops;
	private int userCount;
	private int thinkTime;
	private String testSuiteName;
	private Level logLevel;
	private int logFileSize;
	private int logWriteRate;
	private String testName;
	private String testerName;
	private String testerMail;

	public Configuration() {
		tableDef = "create table configuration (test_name varchar(50), tester_name varchar(20), tester_mail varchar(50),test_suite_name varchar(20), user_count int, duration int, loops int, think_time int, log_level varchar(10), log_file_size int,log_write_rate int)";
	}

	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	public String getTesterMail() {
		return testerMail;
	}

	public void setTesterMail(String testerMail) {
		this.testerMail = testerMail;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getLoops() {
		return loops;
	}

	public void setLoops(int loops) {
		this.loops = loops;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public int getThinkTime() {
		return thinkTime;
	}

	public void setThinkTime(int thinkTime) {
		this.thinkTime = thinkTime;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public Level getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Level logLevel) {
		this.logLevel = logLevel;
	}

	public int getLogFileSize() {
		return logFileSize;
	}

	public void setLogFileSize(int logFileSize) {
		this.logFileSize = logFileSize;
	}

	public int getLogWriteRate() {
		return logWriteRate;
	}

	public void setLogWriteRate(int logWriteRate) {
		this.logWriteRate = logWriteRate;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

}
