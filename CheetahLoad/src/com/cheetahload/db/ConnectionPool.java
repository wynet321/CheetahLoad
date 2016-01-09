package com.cheetahload.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionPool {
	private ConcurrentLinkedQueue<Connection> connectionPool;
	private String dbClassName, url;
	private int queueSize, maxQueueSize;

	public synchronized Connection get() {
		if (connectionPool.isEmpty()) {
			return create();
		} else {
			return connectionPool.poll();
		}
	}

	private Connection create() {
		try {
			Class.forName(dbClassName);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: ConnectionPool - create() - JDBC class not found.");
			e.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url);
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("ERROR: ConnectionPool - create() - Failed to create Connection to DB '" + url + "'.");
			e.printStackTrace();
		}
		return connection;
	}

	public synchronized void release(Connection connection) {
		if (connection != null) {
			connectionPool.add(connection);
			queueSize++;
		}
		while (queueSize > maxQueueSize) {
			try {
				connectionPool.poll().close();
			} catch (SQLException e) {
				System.out.println("ERROR: ConnectionPool - release() - Connection failed to close.");
				e.printStackTrace();
			}
		}
	}

	public ConnectionPool(String dbClassName, String url, int maxQueueSize) {
		if (dbClassName == null || dbClassName.isEmpty()) {
			System.out
					.println("ERROR: ConnectionPool - ConnectionPool(String dbClassName, String url, int queueRefreshRate) - Parameter dbClassName is null or empty.");
		}
		if (url == null || url.isEmpty()) {
			System.out
					.println("ERROR: ConnectionPool - ConnectionPool(String dbClassName, String url, int queueRefreshRate) - Parameter url is null or empty.");
		}
		if (maxQueueSize <= 0) {
			System.out
					.println("ERROR: ConnectionPool - ConnectionPool(String dbClassName, String url, int queueRefreshRate) - Parameter maxQueueSize is negative or zero. Set it to default value 1.");
			this.maxQueueSize = 1;
		}
		this.url = url;
		this.dbClassName = dbClassName;
		this.maxQueueSize=maxQueueSize;
		connectionPool = new ConcurrentLinkedQueue<Connection>();
		queueSize = 0;
	}
}
