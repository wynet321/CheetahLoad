package com.cheetahload.executor;

import java.util.ArrayList;
import java.util.Iterator;

import com.cheetahload.TestCase;
import com.cheetahload.TestConfiguration;
import com.cheetahload.TestScript;
import com.cheetahload.TestSuite;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;

public class TestThread extends Thread {
	TestSuite testSuite;
	private Logger logger;
	private String userName;

	public TestThread(TestSuite testSuite) {
		this.testSuite = testSuite;
		this.userName=TestConfiguration.getUserNames().get(TestConfiguration.getIndex());
		logger=new Logger(TestConfiguration.getLogPath()+this.userName+".log", TestConfiguration.getLogLevel());
	}
	
	public Logger getLogger(){
		return logger;
	}

	public void run() {
		execute(testSuite.getPrepareTestScript());
		execute(testSuite.getTestCaseList());
		execute(testSuite.getClearupTestScript());
		logger.flush(true);
	}

	private void execute(TestScript testScript) {
		// test suite loop
		if (testScript != null) {
			testScript.prepare();
			testScript.test();
			testScript.clearup();
		}
	}

	private void execute(ArrayList<TestCase> testItemList) {
		// loop
		// TODO deal with percentage
		Iterator<TestCase> iterator = testSuite.getTestCaseList().iterator();
		while (iterator.hasNext()) {
			TestCase testcase = iterator.next();
			if (testcase != null)
			{
				//System.out.println(this.getName() + testcase.getTestScript().getName());
				logger.write("TestThread - execute(ArrayList<TestCase>) "+testcase.getTestScript().getName()+" start.",Level.DEBUG);
				execute(testcase.getTestScript());
				logger.write("TestThread - execute(ArrayList<TestCase>) "+testcase.getTestScript().getName()+" end.",Level.DEBUG);
			}

		}
	}
}
