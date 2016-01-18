package com.cheetahload.timer;

import com.cheetahload.TestConfiguration;
import com.cheetahload.executor.TestThread;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;

public final class Transaction {

	private String name;
	private long begin = 0L;
	private long end = 0L;
	private long duration = 0L;
	private String scriptName, vuName;

	public Transaction(String scriptName, String transactionName) {
		if(scriptName==null||scriptName.isEmpty()){
			Logger.get(LoggerName.Common).add("Transaction - Transaction(String scriptName, String transactionName) - Parameter scriptName is null or empty. Set it to default value 'Test'.", Level.WARN);
			scriptName="Test";
		}else{
			this.scriptName=scriptName;
		}
		if (transactionName == null || transactionName.isEmpty()) {
			Logger.get(LoggerName.Common).add("Transaction - Transaction(String scriptName, String transactionName) - Parameter transactionName is null or empty. Set it to default value 'Transaction_[timestamp]'.", Level.WARN);
			this.name = "Transaction_" + System.currentTimeMillis();
		} else {
			this.name = transactionName;
		}
	}

	public String getName() {
		return name;
	}

	public void begin() {
		begin = System.currentTimeMillis();
	}

	public void end() {
		end = System.currentTimeMillis();
		duration = end - begin;
		TestThread current = (TestThread) (Thread.currentThread());
		vuName = current.getUserName();
		String sql = "insert into tranx values('" + scriptName + "','" + vuName + "','" + name + "'," + duration
				+ ")";
		TestConfiguration.getTestConfiguration().getOperator().execute(sql);
	}
}
