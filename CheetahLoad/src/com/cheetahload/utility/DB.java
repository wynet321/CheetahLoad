package com.cheetahload.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestConfiguration;
import com.cheetahload.executor.TestLauncher;
import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.Level;

public class DB {

	private static Connection connection;

	public static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:"
						+ TestConfiguration.getTestConfiguration().getLogPath() + "/"
						+ TestConfiguration.getTestConfiguration().getTestSuiteName() + ".db");
				connection
						.createStatement()
						.executeUpdate(
								"create table timer_"
										+ TestConfiguration.getTestConfiguration().getTestName()
										+ "(testname varchar(50), scriptname varchar(20), duration int, username varchar(20));");
				connection.setAutoCommit(false);
			} catch (Exception e) {
				CommonLogger
						.getCommonLogger()
						.write("DB - getConnection() - Failed to get Connection. Please stop current test since performance data will not record correctly.",
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
			CommonLogger.getCommonLogger().write(
					"DB - closeConnection() - Close DB connection failed! " + e.getMessage(), Level.ERROR);
			e.printStackTrace();
			TestLauncher.stopLogger();
		}
	}

	public static boolean insert(String sql, Vector<String> parameters) {
		PreparedStatement sqlStatement;
		if (null != sql && !sql.isEmpty()) {
			String sqlValue = new String();
			try {
				sqlStatement = getConnection().prepareStatement(sql);
				sqlStatement.clearBatch();
				if (null != parameters && !parameters.isEmpty()) {
					for (String value : parameters) {
						sqlValue += value + "\n";
						String[] valueString = value.split(",");
						sqlStatement.setString(1, valueString[0]);
						sqlStatement.setString(2, valueString[1]);
						sqlStatement.setInt(3, Integer.parseInt(valueString[2]));
						sqlStatement.setString(4, valueString[3]);
						sqlStatement.addBatch();
					}
				}
				sqlStatement.executeBatch();
				connection.commit();
			} catch (SQLException e) {
				CommonLogger
						.getCommonLogger()
						.write("DB - insertBatch() - Execute batch SQL failed. Batch SQL: insert into timer values(?,?,?,?). Please stop current test since performance data can't be record correctly. "
								+ e.getMessage(), Level.ERROR);
				CommonLogger.getCommonLogger().write("DB -  insertBatch() - SQL parameter values:\n" + sqlValue,
						Level.ERROR);
				e.printStackTrace();
				try {
					DB.getConnection().rollback();
				} catch (SQLException e1) {
					CommonLogger.getCommonLogger().write(
							"DB - insertBatch() - Roll back failed. Please check DB connection. " + e.getMessage(),
							Level.ERROR);
					TestLauncher.stopLogger();
					e1.printStackTrace();
				}
				TestLauncher.stopLogger();
				return false;
			}
			return true;
		} else {
			CommonLogger.getCommonLogger().write("DB - insert() - SQL string is null or empty.  ", Level.ERROR);
			return false;
		}
	}

	public static boolean insert(String sql, ConcurrentLinkedQueue<String> queue) {
		if (null == sql || sql.isEmpty()) {
			CommonLogger.getCommonLogger().write("DB - insert() - SQL string is null or empty.", Level.ERROR);
			return false;
		}
		StringBuilder logValue = new StringBuilder();
		try {
			PreparedStatement sqlStatement = getConnection().prepareStatement(sql);
			int batchCount = 0;
			while (!queue.isEmpty()) {
				String[] value = queue.poll().split(",");
				logValue.append(value[0]).append(value[1]).append(value[2]).append(value[3]).append("\n");
				sqlStatement.setString(1, value[0]);
				sqlStatement.setString(2, value[1]);
				sqlStatement.setInt(3, Integer.parseInt(value[2]));
				sqlStatement.setString(4, value[3]);
				sqlStatement.addBatch();
				batchCount++;
				if (batchCount >= 1000) {
					sqlStatement.executeBatch();
					getConnection().commit();
					logValue.delete(0, logValue.length());
				}
			}
			sqlStatement.executeBatch();
			getConnection().commit();

		} catch (SQLException e) {
			CommonLogger.getCommonLogger().write(
					"DB - insertBatch() - Execute batch SQL failed. Batch SQL: " + sql
							+ ". Please stop current test since performance data can't be recorded correctly. "
							+ e.getMessage(), Level.ERROR);
			CommonLogger.getCommonLogger().write("DB -  insertBatch() - SQL parameter values:\n" + logValue,
					Level.ERROR);
			e.printStackTrace();
			try {
				DB.getConnection().rollback();
			} catch (SQLException e1) {
				CommonLogger.getCommonLogger().write(
						"DB - insertBatch() - Roll back failed. Please check DB connection. " + e.getMessage(),
						Level.ERROR);
				TestLauncher.stopLogger();
				e1.printStackTrace();
			}
			TestLauncher.stopLogger();
			return false;
		}
		return true;

	}
}
