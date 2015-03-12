package com.cheetahload;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.Level;

public class TestConfiguration {

	private static int duration = 0;
	private static int loops = 0;
	private static int vusers = 0;
	private static Vector<String> userNames;
	private static String password = new String();
	private static int thinkTime = 0;
	private static String testSuiteName = new String();
	private static String logPath = "./log";
	private static String timerLogPath = "./log/timer/";
	private static Level logLevel = Level.ERROR;
	private static CommonLogger commonLogger;
	private static int userIndex = 0;
	private static HashMap<String, ConcurrentLinkedQueue<String>> timerQueueMap = new HashMap<String, ConcurrentLinkedQueue<String>>();
	private static HashMap<String, ConcurrentLinkedQueue<String>> userLoggerQueueMap = new HashMap<String, ConcurrentLinkedQueue<String>>();

	public static HashMap<String, ConcurrentLinkedQueue<String>> getUserLoggerQueueMap() {
		return userLoggerQueueMap;
	}

	public static boolean initial() {
		TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - duration=" + duration + " seconds", Level.DEBUG);
		TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - loops=" + loops, Level.DEBUG);
		if (duration == 0 && loops == 0) {
			TestConfiguration.getCommonLogger().write("TestConfiguration - isCompleted() - duration or loops should be non-zero value.", Level.ERROR);
			return false;
		}
		if (duration != 0 && loops != 0) {
			TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - duration and loops can not be non-zero both.", Level.ERROR);
			return false;
		}
		TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - vusers=" + vusers, Level.DEBUG);
		if (vusers == 0) {
			TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - vusers should be non-zero value.", Level.ERROR);
			return false;
		}
		TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - password=" + password, Level.DEBUG);
		if (password.isEmpty()) {
			TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - password is set to blank.", Level.WARN);
		}
		TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - testSuiteName=" + testSuiteName, Level.DEBUG);
		if (testSuiteName.isEmpty()) {
			TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - testSuiteName can not be blank.", Level.ERROR);
			return false;
		}
		if (userNames != null)
			TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - userNames has " + userNames.size() + " cell object(s).",
					Level.DEBUG);
		else {
			TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - userNames can not be null.", Level.ERROR);
			return false;
		}
		if (userNames.size() == 0) {
			TestConfiguration.getCommonLogger().write("TestConfiguration - initial() - userNames should has cell object(s).", Level.ERROR);
			return false;
		}

		if (!initialLogPath(timerLogPath)) {
			return false;
		}

		if (!initialLogPath(logPath)) {
			return false;
		}

		TestConfiguration.getCommonLogger().write("TestConfiguration - initial() done.", Level.DEBUG);
		return true;
	}

	private static boolean initialLogPath(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			if (dir.isDirectory()) {
				if (!clearDirectory(new File(path))) {
					throw new RuntimeException("TestConfiguration - initialLogPath() Clear folder '" + path + "' failed. Please clear by manual.");
				}
			} else {
				if (!dir.delete())
					throw new RuntimeException("TestConfiguration - initialLogPath() Delete file '" + path + "' failed. Please delete by manual.");
				if (!dir.mkdir())
					throw new RuntimeException("TestConfiguration - initialLogPath() Create folder '" + path + "' failed. Please create by manual.");
			}
			return true;
		} else {
			if (dir.mkdirs()) {
				return true;
			} else {
				throw new RuntimeException("TestConfiguration - initialLogPath() Create folder '" + path + "' failed. Please check permission.");
			}
		}
	}

	public static String getTimerLogPath() {
		return timerLogPath;
	}

	public static String getLogPath() {
		return logPath;
	}

	private static boolean clearDirectory(File dir) {
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

	public static void setTimerLogPath(String timerLogPath) {
		TestConfiguration.timerLogPath = timerLogPath;
	}

	public static HashMap<String, ConcurrentLinkedQueue<String>> getTimerQueueMap() {
		return timerQueueMap;
	}

	public synchronized static int getUserIndex() {
		return userIndex++;
	}

	public static Vector<String> getUserNames() {
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
		TestConfiguration.duration = duration * 60; // transfer minutes to
													// seconds
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

	public static void setLogLocation(String logLocation) {
		TestConfiguration.logPath = logLocation;
	}

	public static Level getLogLevel() {
		return logLevel;
	}

	public static void setLogLevel(Level logLevel) {
		TestConfiguration.logLevel = logLevel;
	}

	public static CommonLogger getCommonLogger() {
		if (commonLogger == null) {
			commonLogger = new CommonLogger();
		}
		return commonLogger;

	}

}
