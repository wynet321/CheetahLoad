package com.cheetahload.executor;

import java.util.ArrayList;
import java.util.Iterator;

import com.cheetahload.TestCase;
import com.cheetahload.TestScript;
import com.cheetahload.TestSuite;
import com.cheetahload.log.Logger;

public class TestThread extends Thread {

	TestSuite testSuite;

	public TestThread(TestSuite testSuite) {
		this.testSuite = testSuite;
	}

	public void run() {
		// Iterator<TestCase>
		// iterator=testSuite.getTestCaseNameList().iterator();
		// while(iterator.hasNext())
		// System.out.println(this.getName()+((TestCase)iterator.next()).getName());

		execute(testSuite.getPrepareTestCase());
		execute(testSuite.getTestItemList());
		execute(testSuite.getClearupTestCase());
		
	}

	private void execute(TestScript testCase) {
		// test suite loop
		if (testCase != null) {
			testCase.prepare();
			testCase.test();
			testCase.clearup();
		}
	}

	private void execute(ArrayList<TestCase> testItemList) {
		// loop
		Iterator<TestCase> iterator = testSuite.getTestItemList().iterator();
		while (iterator.hasNext()){
			TestCase testcase=iterator.next();
			if(testcase!=null)
				System.out.println(this.getName() + testcase.getTestScript().getName());
			
		}
	}
}
