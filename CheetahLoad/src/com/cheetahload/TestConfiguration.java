package com.cheetahload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.Level;

public class TestConfiguration {

	private int duration;
	private int loops;
	private int vusers;
	private Vector<String> userNames;
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
		vusers = 0;
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

	public boolean verifyConfiguration() {
		CommonLogger commonLogger = CommonLogger.getCommonLogger();

		commonLogger.write("TestConfiguration - initial() - duration=" + duration + " seconds", Level.DEBUG);
		commonLogger.write("TestConfiguration - initial() - loops=" + loops, Level.DEBUG);
		if (duration == 0 && loops == 0) {
			commonLogger.write("TestConfiguration - isCompleted() - duration or loops should be non-zero value.",
					Level.ERROR);
			return false;
		}
		if (duration != 0 && loops != 0) {
			commonLogger.write("TestConfiguration - initial() - duration and loops can not be non-zero both.",
					Level.ERROR);
			return false;
		}
		commonLogger.write("TestConfiguration - initial() - vusers=" + vusers, Level.DEBUG);
		if (vusers == 0) {
			commonLogger.write("TestConfiguration - initial() - vusers should be non-zero value.", Level.ERROR);
			return false;
		}
		commonLogger.write("TestConfiguration - initial() - password=" + password, Level.DEBUG);
		if (password.isEmpty()) {
			commonLogger.write("TestConfiguration - initial() - password is set to blank.", Level.WARN);
		}
		commonLogger.write("TestConfiguration - initial() - testSuiteName=" + testSuiteName, Level.DEBUG);
		if (testSuiteName.isEmpty()) {
			commonLogger.write("TestConfiguration - initial() - testSuiteName can not be blank.", Level.ERROR);
			return false;
		}
		if (userNames != null)
			if (userNames.size() != 0) {
				commonLogger.write("TestConfiguration - initial() - userNames vector has " + userNames.size()
						+ " cell object(s).", Level.DEBUG);
			} else {
				commonLogger.write("TestConfiguration - initial() - userNames vector should include cell object(s).",
						Level.ERROR);
				return false;
			}
		else {
			commonLogger.write("TestConfiguration - initial() - userNames vector can not be null.", Level.ERROR);
			return false;
		}

		if (!initialLogPath(timerLogPath)) {
			return false;
		}

		if (!initialLogPath(logPath)) {
			return false;
		}

		commonLogger.write("TestConfiguration - initial() done.", Level.DEBUG);
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

	public Vector<String> getUserNames() {
		return userNames;
	}

	public void setVusers(int vusers, String prefix, int digit, int startNumber) {
		this.vusers = vusers;
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
		return vusers;
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
