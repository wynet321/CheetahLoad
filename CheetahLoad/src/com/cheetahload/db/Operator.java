package com.cheetahload.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

public class Operator {
	private static ConnectionPool connectionPool;
	private static Operator operator;

	public static Operator getOperator(String dbClassName, String url, int maxQueueSize) {
		if (dbClassName == null || dbClassName.isEmpty()) {
			System.out
					.println("Error: Operator - getOperator(String dbClassName, String url, int maxQueueSize) - Parameter dbClassName is null or empty. Connection pool creation failed.");
		} else if (url == null || url.isEmpty()) {
			System.out
					.println("Error: Operator - getOperator(String dbClassName, String url, int maxQueueSize) - Parameter url is null or empty. Connection pool creation failed.");
		} else {
			connectionPool = new ConnectionPool(dbClassName, url, maxQueueSize);
		}
		if (operator == null) {
			operator = new Operator();
		}
		return operator;
	}

	public boolean insert(List<String> sqlList) {
		if (null == sqlList || sqlList.isEmpty()) {
			System.out.println("ERROR: Operator - insert(List<String> sqlList) - SQL list is null or empty.");
			return false;
		}
		Connection connection = connectionPool.get();
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
					System.out
							.println("ERROR: Operator - insert(List<String> sqlList) - Failed to add batch to create tables. SQL: '"
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
			connectionPool.release(connection);
		}
		return true;
	}

	public boolean insert(String sql) {
		if (null == sql || sql.isEmpty()) {
			System.out.println("ERROR: Operator - insert(String sql) - SQL string is null or empty.");
			return false;
		}
		Connection connection = connectionPool.get();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.execute(sql);
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ERROR: Operator - insert(String sql) - execute SQL statement failed. SQL: '" + sql
					+ "'");
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
					System.out
							.println("ERROR: Operator - createTables(List<String> tableDefinitions) - Failed to close statement.");
					e.printStackTrace();
				}
			}
			connectionPool.release(connection);
		}
		return true;
	}
}
