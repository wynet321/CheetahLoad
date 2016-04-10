package main.resources.com.cheetahload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import main.resources.com.cheetahload.db.JDBCOperator;
import main.resources.com.cheetahload.log.Level;
import main.resources.com.cheetahload.log.Logger;
import main.resources.com.cheetahload.log.LoggerName;

public class TestConfiguration {

	private int wholeTestDuration;
	private int loops;
	private int userCount;
	private List<String> userNames;
	private String password;
	private int thinkTime;
	private String testSuiteName;
	private String logPath;
	private Level logLevel;
	private int userIndex;
	private int logFileSize;
	private static TestConfiguration testConfiguration;
	private int logWriteCycleTime;
	private String testName;
	private String testerName;
	private String testerMail;
	private boolean randomThinkTime;
	private Logger logger;
	private String[] dbParameters;
	private JDBCOperator operator;

	public JDBCOperator getOperator() {
		if (operator == null) {
			operator = JDBCOperator.getOperator(dbParameters);

		}
		return operator;
	}

	public String[] getDbParameters() {
		return dbParameters;
	}

	public void setDbParameters(String[] dbParameters) {
		this.dbParameters = dbParameters;
	}

	// public String getConnectionString() {
	// return connectionString;
	// }

	// private int maxConnectionPoolSize;
	// public int getMaxConnectionPoolSize() {
	// return maxConnectionPoolSize;
	// }
	//
	// private String dbClassName;
	//
	// public String getDbClassName() {
	// return dbClassName;
	// }
	//
	// public void setConnectionString(String connectionString) {
	// this.connectionString = connectionString;
	// }

	// public void setMaxConnectionPoolSize(int maxConnectionPoolSize) {
	// this.maxConnectionPoolSize = maxConnectionPoolSize;
	// }
	//
	// public void setDbClassName(String dbClassName) {
	// this.dbClassName = dbClassName;
	// }

	private TestConfiguration() {
		// default value
		wholeTestDuration = 0;
		loops = 0;
		userCount = 0;
		password = new String();
		thinkTime = 0;
		testSuiteName = new String();
		logPath = "./log";
		logLevel = Level.ERROR;
		userIndex = 0;
		logFileSize = 1024000;
		logWriteCycleTime = 30000;
		testName = new SimpleDateFormat("yyyy_MM_dd.HH_mm_ss").format(System.currentTimeMillis());
		testerName = "";
		testerMail = "";
		randomThinkTime = false;
		logger = Logger.get(LoggerName.Common);
		// by default, use SQLite as DB.
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:sqlite:").append(logPath).append("/").append(testName).append(".db");
		dbParameters = new String[] { "org.sqlite.JDBC", sb.toString(), "1" };
	}

	public boolean isRandomThinkTime() {
		return randomThinkTime;
	}

	public void setRandomThinkTime(boolean randomThinkTime) {
		this.randomThinkTime = randomThinkTime;
	}

	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		if (null != testerName && !testerName.isEmpty()) {
			this.testerName = testerName;
		}
	}

	public String getTesterMail() {
		return testerMail;
	}

	public void setTesterMail(String testerMail) {
		if (null != testerMail && !testerMail.isEmpty()) {
			this.testerMail = testerMail;
		}
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		if (null != testName && !testName.isEmpty()) {
			this.testName = testName + "_" + this.testName;
		}
	}

	public int getLogFileSize() {
		return logFileSize;
	}

	public void setLogFileSize(int logFileSize) {
		if (logFileSize > 0) {
			this.logFileSize = logFileSize;
		}
	}

	public static TestConfiguration getTestConfiguration() {
		if (testConfiguration == null) {
			testConfiguration = new TestConfiguration();
		}
		return testConfiguration;
	}

	public int getLogWriteCycleTime() {
		return logWriteCycleTime;
	}

	public void setLogWriteCycleTime(int logWriteCycleTime) {
		if (logWriteCycleTime > 0) {
			this.logWriteCycleTime = logWriteCycleTime * 1000;
		}
	}

	public boolean verify() {
		logger.add("TestConfiguration - verify() - wholeTestDuration=" + wholeTestDuration + " seconds", Level.DEBUG);
		logger.add("TestConfiguration - verify() - loops=" + loops, Level.DEBUG);
		if (testName.isEmpty()) {
			logger.add("TestConfiguration - verify() - Tester name must be set.", Level.ERROR);
			return false;
		}
		if (wholeTestDuration == 0 && loops == 0) {
			logger.add("TestConfiguration - verify() - wholeTestDuration or loops should be non-zero value.",
					Level.ERROR);
			return false;
		}
		logger.add("TestConfiguration - verify() - vusers=" + userCount, Level.DEBUG);
		if (userCount == 0) {
			logger.add("TestConfiguration - verify() - vusers should be non-zero value.", Level.ERROR);
			return false;
		}
		logger.add("TestConfiguration - verify() - password=" + password, Level.DEBUG);
		if (password.isEmpty()) {
			logger.add("TestConfiguration - verify() - password is set to blank.", Level.WARN);
		}
		logger.add("TestConfiguration - verify() - testSuiteName=" + testSuiteName, Level.DEBUG);
		if (testSuiteName.isEmpty()) {
			logger.add("TestConfiguration - verify() - testSuiteName can not be blank.", Level.ERROR);
			return false;
		}
		if (userNames != null)
			if (userNames.size() != 0) {
				logger.add(
						"TestConfiguration - verify() - userNames vector has " + userNames.size() + " cell object(s).",
						Level.DEBUG);
			} else {
				logger.add("TestConfiguration - verify() - userNames vector should include cell object(s).",
						Level.ERROR);
				return false;
			}
		else {
			logger.add("TestConfiguration - verify() - userNames vector can not be null.", Level.ERROR);
			return false;
		}

		if (!initialLogPath(logPath)) {
			return false;
		}

		logger.add("TestConfiguration - verify() - Parameters verification completed.", Level.DEBUG);
		return true;
	}

	private boolean initialLogPath(String path) {
		if (null == path || path.isEmpty()) {
			logger.add("TestConfiguration - initialLogPath(String path) - Parameter path is null or empty.",
					Level.ERROR);
			return false;
		}
		File dir = new File(path);
		if (dir.exists()) {
			if (dir.isDirectory()) {
				if (!clearDirectory(new File(path))) {
					logger.add("TestConfiguration - initialLogPath(String path) Clear folder '" + path
							+ "' failed. Please clear by manual.", Level.ERROR);
				}
				logger.add("TestConfiguration - initialLogPath(String path) - Directory '" + path + "' was cleared up.",
						Level.DEBUG);
			} else {
				if (!dir.delete())
					logger.add("TestConfiguration - initialLogPath(String path) Delete file '" + path
							+ "' failed. Please delete by manual.", Level.ERROR);
				if (!dir.mkdir())
					logger.add("TestConfiguration - initialLogPath(String path) Create folder '" + path
							+ "' failed. Please create by manual.", Level.ERROR);
			}
			return true;
		} else {
			if (dir.mkdirs()) {
				return true;
			} else {
				logger.add("TestConfiguration - initialLogPath(String path) Create folder '" + path
						+ "' failed. Please check permission.", Level.ERROR);
				return false;
			}
		}
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

	public synchronized int getUserIndex() {
		return userIndex++;
	}

	public List<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(int userCount, String prefix, int digit, int startNumber) {
		if (userCount > 0 && prefix != null && digit > 0 && startNumber >= 0) {
			this.userCount = userCount;
			userNames = VirtualUser.generateUserNames(prefix, digit, startNumber, userCount);
		}
	}

	public int getWholeTestDuration() {
		return wholeTestDuration;
	}

	public void setWholeTestDuration(int wholeTestDuration) {
		if (wholeTestDuration > 0) {
			// transfer minutes to seconds
			this.wholeTestDuration = wholeTestDuration * 60;
		}
	}

	public int getLoops() {
		return loops;
	}

	public void setLoops(int loops) {
		if (loops > 0) {
			this.loops = loops;
		}
	}

	public int getUserCount() {
		return userCount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password != null && !password.isEmpty()) {
			this.password = password;
		}
	}

	public int getThinkTime() {
		return thinkTime;
	}

	public void setThinkTime(int thinkTime) {
		if (thinkTime > 0) {
			this.thinkTime = thinkTime;
		} else {
			this.thinkTime = 0;
		}
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		if (testSuiteName != null && !testSuiteName.isEmpty()) {
			this.testSuiteName = testSuiteName;
		}
	}

	public void setLogLocation(String logLocation) {
		if (logLocation != null && !logLocation.isEmpty()) {
			this.logPath = logLocation;
		}
	}

	public Level getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Level logLevel) {
		this.logLevel = logLevel;
	}

}
