package com.cheetahload.db;

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
	private final static String TIMERTABLEDEFINITION = "create table timer (testname varchar(50), scriptname varchar(20), username varchar(20),duration int)";
	private final static String CONFIGURATIONTABLEDEFINITION = "create table configuration (test_name varchar(50), tester_name varchar(20), tester_mail varchar(50),test_suite_name varchar(20), user_count int, duration int, loops int, think_time int, log_level varchar(10), log_file_size int,log_write_rate int)";

	private static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				StringBuilder connectionString = new StringBuilder();
				connectionString.append("jdbc:sqlite:").append(TestConfiguration.getTestConfiguration().getLogPath())
						.append("/").append(TestConfiguration.getTestConfiguration().getTestName()).append(".db");
				connection = DriverManager.getConnection(connectionString.toString());
				connection.createStatement().executeUpdate(TIMERTABLEDEFINITION);
				connection.createStatement().executeUpdate(CONFIGURATIONTABLEDEFINITION);
				connection.setAutoCommit(false);
			} catch (ClassNotFoundException e) {
				Logger.get(LoggerName.Common).write(
						"DB - getConnection() - JDBC class not found. Need to stop current test. " + e.getMessage(),
						Level.ERROR);
				e.printStackTrace();
				TestLauncher.stopLogger();
			} catch (Exception e) {
				Logger.get(LoggerName.Common).write(
						"DB - getConnection() - Failed to create Connection to DB. Need to stop current test. "
								+ e.getMessage(), Level.ERROR);
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
			Logger.get(LoggerName.Common).write(
					"DB - closeConnection() - Close DB connection failed! " + e.getMessage(), Level.ERROR);
			e.printStackTrace();
			TestLauncher.stopLogger();
		}
	}

	public static boolean insert(String sql) {
		boolean result=false;
		if (null == sql || sql.isEmpty()) {
			Logger.get(LoggerName.Common).write("DB - insert() - SQL string is null or empty.", Level.DEBUG);
			return result;
		}

		Statement sqlStatement = null;
		try {
			sqlStatement = getConnection().createStatement();
		} catch (SQLException e) {
			Logger.get(LoggerName.Common)
					.write("DB - insert() - Create SQL statement failed. Please stop current test since performance data can't be recorded correctly. "
							+ e.getMessage(), Level.ERROR);
			e.printStackTrace();
			return result;
		}
		try {
			result=sqlStatement.execute(sql);
			getConnection().commit();
		} catch (SQLException e) {
			Logger.get(LoggerName.Common)
					.write("DB - insert() - Execute SQL batch statement failed. Part of performance data can't be recorded. Recommend stop current test."
							+ e.getMessage(), Level.ERROR);
			e.printStackTrace();
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				Logger.get(LoggerName.Common).write(
						"DB - insertBatch() - Roll back failed. Please check DB connection. " + e.getMessage(),
						Level.ERROR);
				TestLauncher.stopLogger();
				e1.printStackTrace();
				result=false;
			}
			result=false;
		}
		return result;
	}

	public static boolean insert(List<String> list) {
		boolean result=false;
		if (null == list || list.isEmpty()) {
			Logger.get(LoggerName.Common).write("DB - insert() - SQL list is null or empty.", Level.DEBUG);
			return result;
		}

		Statement sqlStatement = null;
		try {
			sqlStatement = getConnection().createStatement();
		} catch (SQLException e) {
			Logger.get(LoggerName.Common)
					.write("DB - insert() - Create SQL statement failed. Please stop current test since performance data can't be recorded correctly. "
							+ e.getMessage(), Level.ERROR);
			e.printStackTrace();
			return result;
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
				return result;
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
						getConnection().rollback();
					} catch (SQLException e1) {
						Logger.get(LoggerName.Common).write(
								"DB - insertBatch() - Roll back failed. Please check DB connection. " + e.getMessage(),
								Level.ERROR);
						TestLauncher.stopLogger();
						e1.printStackTrace();
						return result;
					}
					return result;
				}
				batchCount = 0;
			}
		}
		try {
			sqlStatement.executeBatch();
			getConnection().commit();
			result=true;
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
				return result;
			}
			return result;
		}
		return result;
	}

}
