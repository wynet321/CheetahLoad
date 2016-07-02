package main.resources.com.cheetahload.db;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mongodb.MongoClient;

public class MongoOperator {

	private static ConcurrentLinkedQueue<MongoClient> connectionPool;
	private volatile int poolSize;
	private static int maxConnectionPoolSize;
	private static String[] parameters;
	private static MongoOperator operator;

	private MongoOperator(String[] parameters) {
		if (parameters == null) {
			System.out.println("ERROR: JDBCOperator - JDBCOperator() - DB parameters is null.");
		}
		MongoOperator.parameters = parameters;
		maxConnectionPoolSize = Integer.parseInt(parameters[2]);
		connectionPool = new ConcurrentLinkedQueue<MongoClient>();
		poolSize = 0;
	}

	public static MongoOperator getOperator(String[] parameters) {
		if (null == operator) {
			operator = new MongoOperator(parameters);
		}
		return operator;
	}

	private MongoClient create() {
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(parameters[0], Integer.parseInt(parameters[1]));
		} catch (NumberFormatException e) {
			System.out.println("ERROR: MongoConnection - create() - Failed to create Connection to DB. Host: "
					+ parameters[0] + ", Port: " + parameters[1] + ".");
			e.printStackTrace();
			return null;

		} catch (UnknownHostException e) {
			System.out.println("ERROR: MongoConnection - create() - Failed to create Connection to DB. Host: "
					+ parameters[0] + ", Port: " + parameters[1] + ".");
			e.printStackTrace();
			return null;
		}
		return mongoClient;
	}

	private MongoClient get() {
		MongoClient mongoClient = connectionPool.poll();
		if (mongoClient == null) {
			mongoClient = create();
			return mongoClient;
		}
		poolSize--;
		return mongoClient;
	}

	public void release(MongoClient mongoClient) {
		if (mongoClient != null) {
			connectionPool.offer(mongoClient);
			poolSize++;
		}
		// System.out.println("DEBUG: ConnectionPool - release() - Current pool
		// size is " + queueSize);
		while (poolSize > maxConnectionPoolSize) {
			try {
				connectionPool.poll().close();
				poolSize--;
			} catch (Exception e) {
				System.out.println("ERROR: ConnectionPool - release() - Connection failed to close.");
				e.printStackTrace();
			}
		}
	}

	public boolean execute(List<String> sqlList) {
		// TODO Auto-generated method stub
		MongoClient client=get();
		return false;
	}

	public boolean execute(String sql) {
		// TODO Auto-generated method stub
		return false;
	}

}
