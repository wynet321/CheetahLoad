package com.cheetahload.db;

import java.util.concurrent.ConcurrentLinkedQueue;

public class OperatorFactory {
	private ConcurrentLinkedQueue<Operator> operatorQueue;
	private String dbClassName, url;
	private int queueSize, maxQueueSize;

	public synchronized Operator getOperator() {
		if (operatorQueue.isEmpty()) {
			return new Operator(dbClassName, url);
		} else {
			return operatorQueue.poll();
		}
	}

	public synchronized void releaseOperator(Operator operator) {
		if (operator != null) {
			operatorQueue.add(operator);
			queueSize++;
		}
		while(queueSize>maxQueueSize){
			operatorQueue.poll().close();
		}
	}

	public OperatorFactory(String dbClassName, String url, int maxQueueSize) {
		// connectionString = new StringBuilder();
		// connectionString.append("jdbc:sqlite:").append(TestConfiguration.getTestConfiguration().getLogPath())
		// .append("/").append(TestConfiguration.getTestConfiguration().getTestName()).append(".db");
		// dbClassName = "org.sqlite.JDBC";
		if(dbClassName==null||dbClassName.isEmpty()){
			System.out.println("ERROR: OperatorFactory - OperatorFactory(String dbClassName, String url, int queueRefreshRate) - Parameter dbClassName is null or empty.");
		}
		if(url==null||url.isEmpty()){
			System.out.println("ERROR: OperatorFactory - OperatorFactory(String dbClassName, String url, int queueRefreshRate) - Parameter url is null or empty.");
		}
		if(maxQueueSize<=0){
			System.out.println("ERROR: OperatorFactory - OperatorFactory(String dbClassName, String url, int queueRefreshRate) - Parameter maxQueueSize is negative or zero. Set it to default value 1.");
			this.maxQueueSize=1;
		}
		this.url = url;
		this.dbClassName = dbClassName;
		operatorQueue = new ConcurrentLinkedQueue<Operator>();
		queueSize = 0;
	}

}
