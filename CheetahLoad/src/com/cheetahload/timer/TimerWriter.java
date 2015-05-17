package com.cheetahload.timer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import com.cheetahload.TestResult;
import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.LogLevel;
import com.cheetahload.utility.DB;

public final class TimerWriter extends Thread {

	private boolean stopSignal;
	private Vector<String> timerVector;
	private TestResult result;
	// private FileWriter logWriter;
	private int logToFileRate;
	private Connection connection;
	private static TimerWriter timerWriter;

	public static TimerWriter getTimerWriter() {
		if (timerWriter == null)
			timerWriter = new TimerWriter();
		return timerWriter;
	}

	private TimerWriter() {
		result = TestResult.getTestResult();
		stopSignal = false;
		timerVector = new Vector<String>();
		logToFileRate = result.getLogToFileRate();
		try {
			connection = DB.getChildDBConnection();
		} catch (ClassNotFoundException e) {
			CommonLogger.getLogger().write(
					"TimerWriter - TimerWriter() SQLite JBDC class can not be found. Stop record time. "
							+ e.getMessage(), LogLevel.ERROR);
			this.interrupt();
		} catch (SQLException e1) {
			CommonLogger.getLogger().write(
					"TimerWriter - TimerWriter() Failed to get general DB connection. Stop record time. "
							+ e1.getMessage(), LogLevel.ERROR);
			this.interrupt();
		}
	}

	public void stopWriter() {
		stopSignal = true;
	}

	public void run() {
		while (!stopSignal) {
			// while (true) {
			writeToDB();
			try {
				sleep(logToFileRate);
			} catch (InterruptedException e) {
				CommonLogger.getLogger().write(
						"TimerWriter - run() Failed to sleep TimerWriter thread. " + e.getMessage(), LogLevel.ERROR);
			}
		}
		// write left timer buffer to db
		writeToDB();
		try {
			connection.close();
		} catch (SQLException e) {
			CommonLogger.getLogger().write(
					"DB - getGeneralDBConnection() Failed to close DB connection. " + e.getMessage(), LogLevel.ERROR);
		}
	}

	// public void writeToFile() {
	// for (String key : result.getTimerBufferKeySet()) {
	// String path = config.getTimerLogPath() + "/" + key + ".log";
	// File file = new File(path);
	// list = result.getTimerList(key);
	// int bufferLength = list.length();
	// try {
	// if (file.exists()) {
	// logWriter = new FileWriter(path, true);
	// } else {
	// logWriter = new FileWriter(path, false);
	// }
	// logWriter.write(list.substring(0, bufferLength));
	// list.delete(0, bufferLength);
	// logWriter.flush();
	// logWriter.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

	public void writeToDB() {
		for (String scriptName : result.getTimerBufferKeySet()) {
			timerVector = result.getTimerVector(scriptName);
			Vector<String> subVector = new Vector<String>(timerVector);
			for (String value : subVector) {
				// value includes duration, username, start time, end time
				String sql = "insert into timer values('" + scriptName + "'," + value + ");";
				try {
					connection.prepareStatement(sql).executeUpdate();
				} catch (SQLException e) {
					CommonLogger.getLogger().write(
							"TimerWriter - writeToDB() execute SQL failed. SQL: " + sql + ". " + e.getMessage(),
							LogLevel.ERROR);
				}
			}
			try {
				connection.commit();
			} catch (SQLException e) {
				CommonLogger.getLogger().write("TimerWriter - writeToDB() Commit to DB failed. " + e.getMessage(),
						LogLevel.ERROR);
				CommonLogger.getLogger().write("TimerWriter - writeToDB() Data lost from DB: ", LogLevel.ERROR);
				for (String value : subVector) {
					CommonLogger.getLogger().write(scriptName + " " + value, LogLevel.ERROR);
				}
				try {
					connection.rollback();
				} catch (SQLException e1) {
					CommonLogger.getLogger().write(
							"TimerWriter - writeToDB() Commit roll back failed. " + e1.getMessage(), LogLevel.ERROR);
				}
			}
			timerVector.removeAll(subVector);
		}
	}
}
