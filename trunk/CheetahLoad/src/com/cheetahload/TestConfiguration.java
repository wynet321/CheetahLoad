package com.cheetahload;

import java.util.Vector;

import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;

public class TestConfiguration {

	private static int duration = 0;
	private static int loops = 0;
	private static int vusers = 0;
	private static Vector<String> userNames;
	private static String password = "";
	private static int thinkTime = 0;
	private static String testSuiteName = "";
	private static String logPath = "./cheetahload.log";
	private static Level logLevel = Level.ERROR;
	private static Logger logger;

	public static boolean isCompleted() {
		if (duration == 0 && loops == 0) {
			return false;
		}
		if (duration != 0 && loops != 0) {
			return false;
		}
		if (vusers == 0) {
			return false;
		}
		if (password.isEmpty()) {
			;
			// log password is blank.
		}
		if (testSuiteName.isEmpty()) {
			// log suite name is empty
			return false;
		}
		if (userNames == null || userNames.size() == 0) { // log userNames is
															// empty
			return false;
		}
		return true;
	}

	public Vector<String> getUserNames() {
		return userNames;
	}

	public static void setVusers(int vusers, String prefix, int digit, int startNumber) {
		TestConfiguration.vusers = vusers;
		userNames = VirtualUser.generateUserNames(prefix, digit, startNumber, vusers);
	}

	public static int getDuration() {
		return duration;
	}

	public static void setDuration(int duration) {
		TestConfiguration.duration = duration;
	}

	public static int getLoops() {
		return loops;
	}

	public static void setLoops(int loops) {
		TestConfiguration.loops = loops;
	}

	public static int getVusers() {
		return vusers;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		TestConfiguration.password = password;
	}

	public static int getThinkTime() {
		return thinkTime;
	}

	public static void setThinkTime(int thinkTime) {
		TestConfiguration.thinkTime = thinkTime;
	}

	public static String getTestSuiteName() {
		return testSuiteName;
	}

	public static void setTestSuiteName(String testSuiteName) {
		TestConfiguration.testSuiteName = testSuiteName;
	}

	public static String getLogPath() {
		return logPath;
	}

	public static void setLogLocation(String logLocation) {
		TestConfiguration.logPath = logLocation;
	}

	public static Level getLogLevel() {
		return logLevel;
	}

	public static void setLogLevel(Level logLevel) {
		TestConfiguration.logLevel = logLevel;
	}

	public static Logger getLogger() {
		if (logger != null)
			return logger;
		else {
			logger=new Logger(TestConfiguration.logPath, TestConfiguration.logLevel);
			return logger;
		}
	}

}
