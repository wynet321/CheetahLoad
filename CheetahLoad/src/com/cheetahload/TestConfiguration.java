package com.cheetahload;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.LogLevel;
import com.cheetahload.utility.DB;

public class TestConfiguration {

	private int duration;
	private int loops;
	private int virtualUserNum;
	private Vector<String> userNames;
	private String password;
	private int thinkTime;
	private String testSuiteName;
	private int userIndex;
	private static TestConfiguration testConfiguration;
	private String testName;
	private String operatorName;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName + new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(System.currentTimeMillis());
	}

	public static TestConfiguration getTestConfiguration() {
		if (testConfiguration == null) {
			testConfiguration = new TestConfiguration();
		}
		return testConfiguration;
	}

	private TestConfiguration() {
		// default value
		duration = 0;
		loops = 0;
		virtualUserNum = 0;
		password = new String();
		thinkTime = 0;
		testSuiteName = new String();

		// timerLogPath = "./log/timer/";

		userIndex = 0;

		operatorName = "Anonymity";
		testName = testSuiteName + new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(System.currentTimeMillis());
	}

	public boolean verify() {
		CommonLogger.getLogger().write("TestConfiguration - verify() - duration=" + duration + " seconds",
				LogLevel.DEBUG);
		CommonLogger.getLogger().write("TestConfiguration - verify() - loops=" + loops, LogLevel.DEBUG);
		if (duration == 0 && loops == 0) {
			CommonLogger.getLogger().write(
					"TestConfiguration - verify() - duration or loops should be non-zero value.", LogLevel.ERROR);
			return false;
		}
		if (duration != 0 && loops != 0) {
			CommonLogger.getLogger().write(
					"TestConfiguration - verify() - duration and loops can not be non-zero both.", LogLevel.ERROR);
			return false;
		}
		CommonLogger.getLogger().write("TestConfiguration - verify() - vusers=" + virtualUserNum, LogLevel.DEBUG);
		if (virtualUserNum == 0) {
			CommonLogger.getLogger().write("TestConfiguration - verify() - vusers should be non-zero value.",
					LogLevel.ERROR);
			return false;
		}
		CommonLogger.getLogger().write("TestConfiguration - verify() - password=" + password, LogLevel.DEBUG);
		if (password.isEmpty()) {
			CommonLogger.getLogger().write("TestConfiguration - verify() - password is set to blank.", LogLevel.WARN);
		}
		CommonLogger.getLogger().write("TestConfiguration - verify() - testSuiteName=" + testSuiteName, LogLevel.DEBUG);
		if (testSuiteName.isEmpty()) {
			CommonLogger.getLogger().write("TestConfiguration - verify() - testSuiteName can not be blank.",
					LogLevel.ERROR);
			return false;
		}
		if (userNames != null)
			if (userNames.size() != 0) {
				CommonLogger.getLogger().write(
						"TestConfiguration - verify() - userNames vector has " + userNames.size() + " cell object(s).",
						LogLevel.DEBUG);
			} else {
				CommonLogger.getLogger().write(
						"TestConfiguration - verify() - userNames vector should include cell object(s).",
						LogLevel.ERROR);
				return false;
			}
		else {
			CommonLogger.getLogger().write("TestConfiguration - verify() - userNames vector can not be null.",
					LogLevel.ERROR);
			return false;
		}

		if (operatorName.isEmpty()) {
			CommonLogger.getLogger().write("TestConfiguration - verify() - operatorName can not be blank.",
					LogLevel.ERROR);
			return false;
		}

		TestResult result = TestResult.getTestResult();
		String configuration = "duration =" + duration + ";\nloops = " + loops + ";\nvirtualUserNum = "
				+ virtualUserNum + ";\npassword = " + password + ";\nthinkTime = " + thinkTime + ";\ntestSuiteName = "
				+ testSuiteName + ";\nresultPath = " + result.getResultPath() + ";\nlogPath = " + result.getLogPath()
				+ ";\nlogLevel = " + result.getLogLevel() + ";\nlogFileSize = " + result.getLogFileSize()
				+ ";\nlogToFileRate = " + result.getLogToFileRate() + ";\noperatorName = " + operatorName + ";\n";
		String sql = "insert into test values('" + testName + "','" + configuration + "','" + operatorName + "','','','','')";
		try {
			DB.getChildDBConnection().prepareStatement(sql).execute();
			DB.getChildDBConnection().commit();
		} catch (SQLException e) {
			CommonLogger.getLogger().write(
					"TestConfiguration - verify() - DB SQL execution failed. SQL: " + sql + ". " + e.getMessage(),
					LogLevel.ERROR);
			return false;
		} catch (ClassNotFoundException e1) {
			CommonLogger.getLogger().write("TestConfiguration - verify() - DB class was not found. " + e1.getMessage(),
					LogLevel.ERROR);
			return false;
		}

		CommonLogger.getLogger().write("TestConfiguration - verify() done.", LogLevel.DEBUG);
		return true;
	}

	public synchronized int getUserIndex() {
		return userIndex++;
	}

	public Vector<String> getUserNames() {
		return userNames;
	}

	public void setVusers(int vusers, String prefix, int digit, int startNumber) {
		this.virtualUserNum = vusers;
		userNames = VirtualUser.generateUserNames(prefix, digit, startNumber, vusers);
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration * 60; // transfer minutes to seconds
	}

	public int getLoops() {
		return loops;
	}

	public void setLoops(int loops) {
		this.loops = loops;
	}

	public int getVusers() {
		return virtualUserNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

}
