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

	private static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				StringBuilder connectionString = new StringBuilder();
				connectionString.append("jdbc:sqlite:").append(TestConfiguration.getTestConfiguration().getLogPath())
						.append("/").append(TestConfiguration.getTestConfiguration().getTestName()).append(".db");
				connection = DriverManager.getConnection(connectionString.toString());
				createTables();
				connection.setAutoCommit(false);
			} catch (ClassNotFoundException e) {
				Logger.get(LoggerName.Common).write(
						"DB - getConnection() - JDBC class not found. Need to stop current test. " + e.getMessage(),
						Level.ERROR);
				e.printStackTrace();
				TestLauncher.stopLogger();
			} catch (Exception e) {
				Logger.get(LoggerName.Common).write(
						"DB - getConnection() - Failed to create Connection to DB. Need to stop current test. " + e.getMessage(),
						Level.ERROR);
				e.printStackTrace();
				TestLauncher.stopLogger();
			}
		}
		return connection;
	}

	private static void createTables() throws Exception {
		connection.createStatement().executeUpdate(Timer.getTableDef());
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

	public static boolean insert(Entity entity){
		return true;
	}
	public static boolean insert(List<String> list) {
		if (null == list || list.isEmpty()) {
			Logger.get(LoggerName.Common).write("DB - insert() - SQL list is null or empty.", Level.DEBUG);
			return true;
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

}
