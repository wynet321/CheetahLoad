package com.cheetahload.timer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cheetahload.TestConfiguration;
import com.cheetahload.TestResult;
import com.cheetahload.db.Operation;

public final class TimerWriter extends Thread {

	private boolean stopSignal;
	private Vector<String> timerVector;
	private TestConfiguration config;
	private TestResult result;
	private FileWriter logWriter;
	private int logToFileRate;
	private Connection connection;

	public TimerWriter() {
		config = TestConfiguration.getTestConfiguration();
		result = TestResult.getTestResult();
		stopSignal = false;
		timerVector = new Vector<String>();
		logToFileRate = config.getLogToFileRate();
		connection = Operation.getConnection();
	}

	public void setStopSignal(boolean stopSignal) {
		this.stopSignal = stopSignal;
	}

	public void run() {
		while (!stopSignal) {
			try {
				sleep(logToFileRate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// writeToFile();
			writeToDB();
		}
		// write all of timer buffer left to file
		// writeToFile();
		writeToDB();
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public void writeToFile() {
//		for (String key : result.getTimerBufferKeySet()) {
//			String path = config.getTimerLogPath() + "/" + key + ".log";
//			File file = new File(path);
//			list = result.getTimerList(key);
//			int bufferLength = list.length();
//			try {
//				if (file.exists()) {
//					logWriter = new FileWriter(path, true);
//				} else {
//					logWriter = new FileWriter(path, false);
//				}
//				logWriter.write(list.substring(0, bufferLength));
//				list.delete(0, bufferLength);
//				logWriter.flush();
//				logWriter.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

	public void writeToDB() {
		for (String key : result.getTimerBufferKeySet()) {
			timerVector = result.getTimerVector(key);
			Vector<String> subVector=(Vector<String>) timerVector.clone();
			for (String value:subVector) {
				String sql = "insert into timer values('" + config.getTestName() + "','" + key + "'," + value + ")";
				try {
					connection.prepareStatement(sql).executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					try {
						connection.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
			try {
				connection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			timerVector.removeAll(subVector);
		}
	}
}
