package com.cheetahload;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;

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
	private static Logger commonLogger;
	private static int userIndex = 0;
	private static HashMap<String, ConcurrentLinkedQueue<String>> timerQueueMap = new HashMap<String, ConcurrentLinkedQueue<String>>();

	public static String getTimerLogPath() {
		if (!createLogFolder(timerLogPath)) {
			commonLogger.write("TestConfiguration - createLogFolder() Failed to create folder '" + timerLogPath
					+ "'. Default folder './log/timer/' will be used.", Level.WARN);
			timerLogPath = "./log/timer";
			createLogFolder(timerLogPath);
		}
		return timerLogPath;
	}

	private static boolean createLogFolder(String path) {
		File dir = new File(timerLogPath);
		if (dir.isDirectory()) {
			if (!dir.exists()) {
				return dir.mkdirs();
			}
			return true;
		}
		return false;
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

	public static boolean isCompleted() {
		TestConfiguration.getCommonLogger().write("TestConfiguration - isCompleted() - duration=" + duration,
				Level.DEBUG);
		TestConfiguration.getCommonLogger().write("TestConfiguration - isCompleted() - loops=" + loops, Level.DEBUG);
		if (duration == 0 && loops == 0) {
			TestConfiguration.getCommonLogger().write(
					"TestConfiguration - isCompleted() - duration or loops should be non-zero value.", Level.ERROR);
			return false;
		}
		if (duration != 0 && loops != 0) {
			TestConfiguration.getCommonLogger().write(
					"TestConfiguration - isCompleted() - duration and loops can not be non-zero both.", Level.ERROR);
			return false;
		}
		TestConfiguration.getCommonLogger().write("TestConfiguration - isCompleted() - vusers=" + vusers, Level.DEBUG);
		if (vusers == 0) {
			TestConfiguration.getCommonLogger().write(
					"TestConfiguration - isCompleted() - vusers should be non-zero value.", Level.ERROR);
			return false;
		}
		TestConfiguration.getCommonLogger().write("TestConfiguration - isCompleted() - password=" + password,
				Level.DEBUG);
		if (password.isEmpty()) {
			TestConfiguration.getCommonLogger().write("TestConfiguration - isCompleted() - password is set to blank.",
					Level.WARN);
		}
		TestConfiguration.getCommonLogger().write("TestConfiguration - isCompleted() - testSuiteName=" + testSuiteName,
				Level.DEBUG);
		if (testSuiteName.isEmpty()) {
			TestConfiguration.getCommonLogger().write(
					"TestConfiguration - isCompleted() - testSuiteName can not be blank.", Level.ERROR);
			return false;
		}
		if (userNames != null)
			TestConfiguration.getCommonLogger().write(
					"TestConfiguration - isCompleted() - userNames has " + userNames.size() + " cell object(s).",
					Level.DEBUG);
		else {
			TestConfiguration.getCommonLogger().write("TestConfiguration - isCompleted() - userNames can not be null.",
					Level.ERROR);
			return false;
		}
		if (userNames.size() == 0) {
			TestConfiguration.getCommonLogger().write(
					"TestConfiguration - isCompleted() - userNames should has cell object(s).", Level.ERROR);
			return false;
		}
		TestConfiguration.getCommonLogger().write("TestConfiguration - isCompleted() passed.", Level.DEBUG);
		return true;
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
		if (!createLogFolder(logPath)) {
			commonLogger.write("TestConfiguration - createLogFolder() Failed to create folder '" + logPath
					+ "'. Default folder './log/' will be used.", Level.WARN);
			logPath = "./log/";
			createLogFolder(logPath);
		}
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

	public static Logger getCommonLogger() {
		if (commonLogger != null)
			return commonLogger;
		else {
			commonLogger = new Logger(TestConfiguration.logPath + "/cheetahload.log", TestConfiguration.logLevel);
			return commonLogger;
		}
	}

}
