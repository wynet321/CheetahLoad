package com.cheetahload.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import com.cheetahload.TestConfiguration;
import com.cheetahload.executor.TestLauncher;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;

public class DB {

	private static Connection connection;

	public static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager
						.getConnection("jdbc:sqlite:" + TestConfiguration.getTestConfiguration().getLogPath() + "/"
								+ TestConfiguration.getTestConfiguration().getTestName() + ".db");
				connection.createStatement().executeUpdate(
						"create table timer (testname varchar(50), scriptname varchar(20),  username varchar(20),duration int)");
				connection.setAutoCommit(false);
			} catch (Exception e) {
				Logger.get(LoggerName.Common).write(
						"DB - getConnection() - Failed to get Connection. Please stop current test since performance data will not record correctly.",
						Level.ERROR);
				e.printStackTrace();
				TestLauncher.stopLogger();
			}
		}
		return connection;
	}

	public static void closeConnection() {
		if (null == connection)
			return;
		try {
			connection.close();
		} catch (SQLException e) {
			Logger.get(LoggerName.Common)
					.write("DB - closeConnection() - Close DB connection failed! " + e.getMessage(), Level.ERROR);
			e.printStackTrace();
			TestLauncher.stopLogger();
		}
	}

	// public static boolean insert(String sql, Vector<String> parameters) {
	// PreparedStatement sqlStatement;
	// if (null != sql && !sql.isEmpty()) {
	// String sqlValue = new String();
	// try {
	// sqlStatement = getConnection().prepareStatement(sql);
	// sqlStatement.clearBatch();
	// if (null != parameters && !parameters.isEmpty()) {
	// for (String value : parameters) {
	// sqlValue += value + "\n";
	// String[] valueString = value.split(",");
	// sqlStatement.setString(1, valueString[0]);
	// sqlStatement.setString(2, valueString[1]);
	// sqlStatement.setInt(3, Integer.parseInt(valueString[2]));
	// sqlStatement.setString(4, valueString[3]);
	// sqlStatement.addBatch();
	// }
	// }
	// sqlStatement.executeBatch();
	// connection.commit();
	// } catch (SQLException e) {
	// CommonLogger
	// .getCommonLogger()
	// .write("DB - insertBatch() - Execute batch SQL failed. Batch SQL: insert
	// into timer values(?,?,?,?). Please stop current test since performance
	// data can't be record correctly. "
	// + e.getMessage(), Level.ERROR);
	// Logger.getLogger(LoggerName.Common).write("DB - insertBatch() - SQL
	// parameter values:\n"
	// + sqlValue,
	// Level.ERROR);
	// e.printStackTrace();
	// try {
	// DB.getConnection().rollback();
	// } catch (SQLException e1) {
	// Logger.getLogger(LoggerName.Common).write(
	// "DB - insertBatch() - Roll back failed. Please check DB connection. " +
	// e.getMessage(),
	// Level.ERROR);
	// TestLauncher.stopLogger();
	// e1.printStackTrace();
	// }
	// TestLauncher.stopLogger();
	// return false;
	// }
	// return true;
	// } else {
	// Logger.getLogger(LoggerName.Common).write("DB - insert() - SQL string is
	// null or empty. ",
	// Level.ERROR);
	// return false;
	// }
	// }

	public static boolean insert(List<String> list) {
		if (null == list || list.isEmpty()) {
			Logger.get(LoggerName.Common).write("DB - insert() - SQL list is null or empty.", Level.ERROR);
			return false;
		}

		Statement sqlStatement = null;
		try {
			sqlStatement = getConnection().createStatement();
		} catch (SQLException e) {
			Logger.get(LoggerName.Common)
					.write("DB - insert() - Create SQL statement failed. Please stop current test since performance data can't be recorded correctly. "
							+ e.getMessage(), Level.ERROR);
			e.printStackTrace();
			return false;
		}
		int batchCount = 0;
		String sql = new String();
		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			sql = iter.next();
			try {
				sqlStatement.addBatch(sql);
			} catch (SQLException e) {
				Logger.get(LoggerName.Common)
						.write("DB - insert() - Add SQL batch statement failed. Part of performance data can't be recorded. Recommend stop current test."
								+ e.getMessage(), Level.ERROR);
				e.printStackTrace();
				return false;
			}
			batchCount++;
			if (batchCount >= 1000) {
				try {
					sqlStatement.executeBatch();
					getConnection().commit();
				} catch (SQLException e) {
					Logger.get(LoggerName.Common)
							.write("DB - insert() - Execute SQL batch statement failed. Part of performance data can't be recorded. Recommend stop current test. "
									+ e.getMessage(), Level.ERROR);
					e.printStackTrace();
					try {
						DB.getConnection().rollback();
					} catch (SQLException e1) {
						Logger.get(LoggerName.Common).write(
								"DB - insertBatch() - Roll back failed. Please check DB connection. " + e.getMessage(),
								Level.ERROR);
						TestLauncher.stopLogger();
						e1.printStackTrace();
						return false;
					}
					return false;
				}
				batchCount = 0;
			}
		}
		try {
			sqlStatement.executeBatch();
			getConnection().commit();
		} catch (SQLException e) {
			Logger.get(LoggerName.Common)
					.write("DB - insert() - Execute SQL batch statement failed. Part of performance data can't be recorded. Recommend stop current test."
							+ e.getMessage(), Level.ERROR);
			e.printStackTrace();
			try {
				DB.getConnection().rollback();
			} catch (SQLException e1) {
				Logger.get(LoggerName.Common).write(
						"DB - insertBatch() - Roll back failed. Please check DB connection. " + e.getMessage(),
						Level.ERROR);
				TestLauncher.stopLogger();
				e1.printStackTrace();
				return false;
			}
			return false;
		}

		return true;

	}

	// public static boolean insert(String sql, ConcurrentLinkedQueue<String>
	// queue) {
	// if (null == sql || sql.isEmpty()) {
	// Logger.get(LoggerName.Common).write("DB - insert() - SQL string is null
	// or empty.", Level.ERROR);
	// return false;
	// }
	//
	// String logValue = new String();
	// try {
	// PreparedStatement sqlStatement = getConnection().prepareStatement(sql);
	// int batchCount = 0;
	// while (!queue.isEmpty()) {
	// logValue = queue.poll();
	// String[] value = logValue.split(",");
	// sqlStatement.setString(1, value[0]);
	// sqlStatement.setString(2, value[1]);
	// sqlStatement.setInt(4, Integer.parseInt(value[3]));
	// sqlStatement.setString(3, value[2]);
	// sqlStatement.addBatch();
	// batchCount++;
	// if (batchCount >= 1000) {
	// sqlStatement.executeBatch();
	// getConnection().commit();
	// batchCount = 0;
	// }
	// }
	// sqlStatement.executeBatch();
	// getConnection().commit();
	// } catch (SQLException e) {
	// Logger.get(LoggerName.Common)
	// .write("DB - insertBatch() - Execute batch SQL failed. Batch SQL: " + sql
	// + ". Please stop current test since performance data can't be recorded
	// correctly. "
	// + e.getMessage(), Level.ERROR);
	// Logger.get(LoggerName.Common).write("DB - insertBatch() - SQL parameter
	// values:\n" + logValue,
	// Level.ERROR);
	// TestLauncher.stopLogger();
	// e.printStackTrace();
	// try {
	// DB.getConnection().rollback();
	// } catch (SQLException e1) {
	// Logger.get(LoggerName.Common).write(
	// "DB - insertBatch() - Roll back failed. Please check DB connection. " +
	// e.getMessage(),
	// Level.ERROR);
	// TestLauncher.stopLogger();
	// e1.printStackTrace();
	// return false;
	// }
	// return false;
	// }
	// return true;
	// }
}
