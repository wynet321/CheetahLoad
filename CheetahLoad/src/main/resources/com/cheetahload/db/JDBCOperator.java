package main.resources.com.cheetahload.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JDBCOperator {

	private static JDBCOperator operator;
	private static ConcurrentLinkedQueue<Connection> connectionPool;
	private volatile int poolSize;
	private static int maxConnectionPoolSize;
	private static String[] parameters;

	private JDBCOperator(String[] parameters) {
		if (parameters == null) {
			System.out.println("ERROR: JDBCOperator - JDBCOperator() - DB parameters is null.");
		}
		JDBCOperator.parameters = parameters;
		maxConnectionPoolSize = Integer.parseInt(parameters[2]);
		connectionPool=new ConcurrentLinkedQueue<Connection>();
		poolSize=0;
	}

	public static JDBCOperator getOperator(String[] parameters) {
		if (operator == null) {
			operator = new JDBCOperator(parameters);
		}
		return operator;
	}

	private Connection get() {
		Connection connection = connectionPool.poll();
		if (connection == null) {
			connection = create();
			return connection;
		}
		poolSize--;
		return connection;
	}

	private Connection create() {
		try {
			Class.forName(parameters[0]);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: JDBCOperator - create() - JDBC class not found. Class name: " + parameters[0]);
			e.printStackTrace();
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(parameters[1]);
			connection.setAutoCommit(false);
			return connection;
		} catch (SQLException e) {
			System.out.println("ERROR: ConnectionPool - JDBCOperator() - Failed to create Connection to DB '"
					+ parameters[1] + "'.");
			e.printStackTrace();
			return null;
		}
	}

	private void release(Connection connection) {
		if (connection != null) {
			connectionPool.offer(connection);
			poolSize++;
		}
		// System.out.println("DEBUG: ConnectionPool - release() - Current pool
		// size is " + queueSize);
		while (poolSize > maxConnectionPoolSize) {
			try {
				connectionPool.poll().getClass().getDeclaredMethod("close", new Class[] {}).invoke(this,
						new Object[] {});
				poolSize--;
			} catch (Exception e) {
				System.out.println("ERROR: JDBCOperator - release() - Connection failed to close.");
				e.printStackTrace();
			}
		}
	}

	public boolean execute(List<String> sqlList) {
		if (null == sqlList || sqlList.isEmpty()) {
			System.out.println("ERROR: Operator - execute(List<String> sqlList) - SQL list is null or empty.");
			return false;
		}
		Connection connection = get();

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
							"ERROR: Operator - execute(List<String> sqlList) - Failed to add batch to create tables. SQL: '"
									+ sql + "'");
					e.printStackTrace();
					return false;
				}
			}
			statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ERROR: Operator - execute(List<String> sqlList) - execute SQL statements failed.");
			e.printStackTrace();
			return false;
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("ERROR: Operator - execute(List<String> sqlList) - Failed to close statement.");
					e.printStackTrace();
				}
			}
			release(connection);
		}
		return true;
	}

	public boolean execute(String sql) {
		if (null == sql || sql.isEmpty()) {
			System.out.println("ERROR: Operator - execute(String sql) - SQL string is null or empty.");
			return false;
		}
		Connection connection = get();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.execute(sql);
			connection.commit();
		} catch (SQLException e) {
			System.out.println(
					"ERROR: Operator - execute(String sql) - execute SQL statement failed. SQL: '" + sql + "'");
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				System.out.println("ERROR: Operator - execute(String sql) - Roll back failed.");
				e1.printStackTrace();
				return false;
			}
			return false;
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("ERROR: Operator - execute(String sql) - Failed to close statement.");
					e.printStackTrace();
				}
			}
			release(connection);
		}
		return true;
	}
}
