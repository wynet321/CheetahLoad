package com.cheetahload.executor;

import com.cheetahload.TestSuite;

public class TestLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestSuite testSuite= new TestSuite();

		int threadCount = 1;
		int i = 0;
		Thread[] thread = new Thread[2];
		while (i <= threadCount) {
			thread[i] = new TestThread(testSuite);
			thread[i].start();
			i++;
		}

	}

}
