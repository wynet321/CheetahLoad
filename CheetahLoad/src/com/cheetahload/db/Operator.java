package com.cheetahload.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import com.cheetahload.executor.TestLauncher;

public class Operator {
	private Connection connection;

	public Operator(String className, String url) {
		if (connection == null) {
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				System.out.println("ERROR: Operator - Operator(String className, String url) - JDBC class not found.");
				e.printStackTrace();
				TestLauncher.stopLogger();
			}
			try {
				connection = DriverManager.getConnection(url);
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				System.out.println(
						"ERROR: Operator - Operator(String className, String url) - Failed to create Connection to DB '"
								+ url + "'.");
				e.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("ERROR: Operator - close() - Connection failed to close.");
			e.printStackTrace();
		}
	}

	public boolean insert(List<String> sqlList) {
		if (null == sqlList || sqlList.isEmpty()) {
			System.out.println("ERROR: Operator - insert(List<String> sqlList) - SQL list is null or empty.");
			return false;
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Iterator<String> iter = sqlList.iterator();
			String sql;
			while (iter.hasNext()) {
				sql = iter.next();
				try {
					statement.addBatch(sql);
				} catch (SQLException e) {
					System.out.println(
							"ERROR: Operator - insert(List<String> sqlList) - Failed to add batch to create tables. SQL: '"
									+ sql + "'");
					e.printStackTrace();
					return false;
				}
			}
			statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ERROR: Operator - insert(List<String> sqlList) - Failed to create tables.");
			e.printStackTrace();
			return false;
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("ERROR: Operator - insert(List<String> sqlList) - Failed to close statement.");
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public boolean insert(String sql) {
		if (null == sql || sql.isEmpty()) {
			System.out.println("ERROR: Operator - insert(String sql) - SQL string is null or empty.");
			return false;
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.execute(sql);
			connection.commit();
		} catch (SQLException e) {
			System.out
					.println("ERROR: Operator - insert(String sql) - execute SQL statement failed. SQL: '" + sql + "'");
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				System.out.println("ERROR: Operator - insert(String sql) - Roll back failed.");
				e1.printStackTrace();
				return false;
			}
			return false;
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println(
							"ERROR: Operator - createTables(List<String> tableDefinitions) - Failed to close statement.");
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
