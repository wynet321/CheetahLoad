package com.cheetahload.db;

import java.sql.Connection;
import java.sql.DriverManager;

import com.cheetahload.TestConfiguration;
import com.cheetahload.log.CommonLogger;
import com.cheetahload.log.Level;

public class Operation {

	private static Connection connection;

	public static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:" + TestConfiguration.getTestConfiguration().getLogPath()+"/"+TestConfiguration.getTestConfiguration().getTestSuiteName() + ".db");
				connection
						.prepareStatement(
								"create table timer(testname varchar(50), scriptname varchar(20), duration int, username varchar(20), start varchar(20), end varchar(20));")
						.execute();
				connection.setAutoCommit(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				CommonLogger.getCommonLogger().write("Operation - getConnection() - Failed to get Connection.", Level.ERROR);
				e.printStackTrace();
			}
		}
		return connection;
	}
}
