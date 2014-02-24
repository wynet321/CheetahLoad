package com.cheetahload;

import java.util.ArrayList;

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

	private void execute(TestCase testCase) {
		// test suite loop
		if (testCase != null) {
			testCase.prepare();
			testCase.test();
			testCase.clearup();
		}
	}

	private void execute(ArrayList<TestItem> testItemList) {
		// loop

	}
}
