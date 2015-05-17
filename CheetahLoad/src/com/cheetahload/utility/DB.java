package com.cheetahload.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;

public class DB {

	private static Connection childDBConnection;
	private static Connection generalDBConnection;

	private static Connection getGeneralDBConnection() throws ClassNotFoundException, SQLException {
		if (generalDBConnection == null) {
			
				Class.forName("org.sqlite.JDBC");
				generalDBConnection = DriverManager.getConnection("jdbc:sqlite:"
						+ TestResult.getTestResult().getResultPath() + "/general.db");
				generalDBConnection.setAutoCommit(false);
				generalDBConnection.prepareStatement(
						"create table if not exists test (guid text, testname text, time text);").execute();
				generalDBConnection.commit();
			// } catch (ClassNotFoundException e) {
			// CommonLogger.getLogger().write(
			// "DB - getGeneralDBConnection() - SQLite JBDC class can not be found. "
			// + e.getMessage(),
			// LogLevel.ERROR);
			// } catch (SQLException e1) {
			// CommonLogger.getLogger().write(
			// "DB - getGeneralDBConnection() - Failed to get general DB connection. "
			// + e1.getMessage(),
			// LogLevel.ERROR);
			// }
		}
		return generalDBConnection;
	}

	public static Connection getChildDBConnection() throws ClassNotFoundException, SQLException{
		if (childDBConnection == null) {
			String uuid = UUID.randomUUID().toString();

			//try {
				getGeneralDBConnection().prepareStatement(
						"insert into test values('" + uuid + "','"
								+ TestConfiguration.getTestConfiguration().getTestName() + "','"
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())
								+ "');").execute();
				childDBConnection = DriverManager.getConnection("jdbc:sqlite:"
						+ TestResult.getTestResult().getResultPath() + "/" + uuid + ".db");
				childDBConnection.setAutoCommit(false);
				generalDBConnection.commit();
//			} catch (SQLException e) {
//				CommonLogger.getLogger().write(
//						"DB - getChildDBConnection() - Failed to get child DB Connection. " + e.getMessage(),
//						LogLevel.ERROR);
//				try {
//					generalDBConnection.rollback();
//				} catch (SQLException e1) {
//					CommonLogger.getLogger().write(
//							"DB - getChildDBConnection() - Failed to roll back general DB. DB name: general.db. "
//									+ e1.getMessage(), LogLevel.ERROR);
//				}
//			}

			//try {
				childDBConnection.prepareStatement("create table timer(scriptname text, duration integer, username text, starttime text, endtime text);").execute();
				childDBConnection
						.prepareStatement(
								"create table test(testname text not null,  suite text, operator text,starttime text, endtime text, duration integer, comment text);")
						.execute();
				childDBConnection.commit();
//			} catch (SQLException e) {
//				CommonLogger.getLogger().write(
//						"DB - getChildDBConnection() - Failed to get child DB Connection. " + e.getMessage(),
//						LogLevel.ERROR);
//				try {
//					childDBConnection.rollback();
//				} catch (SQLException e1) {
//					CommonLogger.getLogger().write(
//							"DB - getChildDBConnection() - Failed to roll back child DB. DB name: " + uuid + ".db. "
//									+ e1.getMessage(), LogLevel.ERROR);
//				}
//			}

		}
		return childDBConnection;
	}

}
