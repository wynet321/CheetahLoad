package com.cheetahload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;

public class TestConfiguration {

	private int duration;
	private int loops;
	private int userCount;
	private List<String> userNames;
	private String password;
	private int thinkTime;
	private String testSuiteName;
	private String logPath;
	private String timerLogPath;
	private Level logLevel;
	private int userIndex;
	private int logFileSize;
	private static TestConfiguration testConfiguration;
	private int logWriteRate;
	private String testName;

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName + "_" + this.testName;
	}

	public int getLogFileSize() {
		return logFileSize;
	}

	public void setLogFileSize(int logFileSize) {
		this.logFileSize = logFileSize;
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
		userCount = 0;
		password = new String();
		thinkTime = 0;
		testSuiteName = new String();
		logPath = "./log";
		timerLogPath = "./log/timer/";
		logLevel = Level.ERROR;
		userIndex = 0;
		logFileSize = 1024000;
		logWriteRate = 10000;
		testName = new SimpleDateFormat("yyyy_MM_dd.HH_mm_ss").format(System.currentTimeMillis());
	}

	public int getLogWriteRate() {
		return logWriteRate;
	}

	public void setLogWriteRate(int logWriteRate) {
		this.logWriteRate = logWriteRate;
	}

	public boolean verify() {
		Logger logger=Logger.get(LoggerName.Common);

		logger.write("TestConfiguration - verify() - duration=" + duration + " seconds", Level.DEBUG);
		logger.write("TestConfiguration - verify() - loops=" + loops, Level.DEBUG);
		if (duration == 0 && loops == 0) {
			logger.write("TestConfiguration - verify() - duration or loops should be non-zero value.",
					Level.ERROR);
			return false;
		}
		if (duration != 0 && loops != 0) {
			logger.write("TestConfiguration - verify() - duration and loops can not be non-zero both.",
					Level.ERROR);
			return false;
		}
		logger.write("TestConfiguration - verify() - vusers=" + userCount, Level.DEBUG);
		if (userCount == 0) {
			logger.write("TestConfiguration - verify() - vusers should be non-zero value.", Level.ERROR);
			return false;
		}
		logger.write("TestConfiguration - verify() - password=" + password, Level.DEBUG);
		if (password.isEmpty()) {
			logger.write("TestConfiguration - verify() - password is set to blank.", Level.WARN);
		}
		logger.write("TestConfiguration - verify() - testSuiteName=" + testSuiteName, Level.DEBUG);
		if (testSuiteName.isEmpty()) {
			logger.write("TestConfiguration - verify() - testSuiteName can not be blank.", Level.ERROR);
			return false;
		}
		if (userNames != null)
			if (userNames.size() != 0) {
				logger.write("TestConfiguration - verify() - userNames vector has " + userNames.size()
						+ " cell object(s).", Level.DEBUG);
			} else {
				logger.write("TestConfiguration - verify() - userNames vector should include cell object(s).",
						Level.ERROR);
				return false;
			}
		else {
			logger.write("TestConfiguration - verify() - userNames vector can not be null.", Level.ERROR);
			return false;
		}

		if (!initialLogPath(timerLogPath)) {
			return false;
		}

		if (!initialLogPath(logPath)) {
			return false;
		}

		logger.write("TestConfiguration - verify() done.", Level.DEBUG);
		return true;
	}

	private boolean initialLogPath(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			if (dir.isDirectory()) {
				if (!clearDirectory(new File(path))) {
					throw new RuntimeException("TestConfiguration - initialLogPath() Clear folder '" + path
							+ "' failed. Please clear by manual.");
				}
			} else {
				if (!dir.delete())
					throw new RuntimeException("TestConfiguration - initialLogPath() Delete file '" + path
							+ "' failed. Please delete by manual.");
				if (!dir.mkdir())
					throw new RuntimeException("TestConfiguration - initialLogPath() Create folder '" + path
							+ "' failed. Please create by manual.");
			}
			return true;
		} else {
			if (dir.mkdirs()) {
				return true;
			} else {
				throw new RuntimeException("TestConfiguration - initialLogPath() Create folder '" + path
						+ "' failed. Please check permission.");
			}
		}
	}

	public String getTimerLogPath() {
		return timerLogPath;
	}

	public String getLogPath() {
		return logPath;
	}

	private boolean clearDirectory(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = clearDirectory(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		} else {
			return dir.delete();
		}
		return true;
	}

	public void setTimerLogPath(String timerLogPath) {
		this.timerLogPath = timerLogPath;
	}

	public synchronized int getUserIndex() {
		return userIndex++;
	}

	public List<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(int userCount, String prefix, int digit, int startNumber) {
		this.userCount = userCount;
		userNames = VirtualUser.generateUserNames(prefix, digit, startNumber, userCount);
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

	public int getUserCount() {
		return userCount;
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

	public void setLogLocation(String logLocation) {
		this.logPath = logLocation;
	}

	public Level getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Level logLevel) {
		this.logLevel = logLevel;
	}

}
