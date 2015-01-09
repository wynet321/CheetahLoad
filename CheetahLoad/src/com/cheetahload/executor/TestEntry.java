package com.cheetahload.executor;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cheetahload.TestCase;
import com.cheetahload.TestConfiguration;
import com.cheetahload.TestScript;
import com.cheetahload.TestSuite;
import com.cheetahload.log.Level;
import com.cheetahload.log.UserLoggerWriter;
import com.cheetahload.timer.TimerWriter;

public final class TestEntry {

	public final static void runTest(TestSuite testSuite) {
		if (!TestConfiguration.initial()) {
			TestConfiguration.getCommonLogger().write(
					"TestEntry - runTest() Test configuration settings are not completed. Test can't start! ",
					Level.ERROR);
			TestConfiguration.getCommonLogger().flush(true);
			System.exit(0);
		}

		// timer log thread
		TestScript prepareTestCase=testSuite.getPrepareTestCase();
		if(prepareTestCase!=null){
			TestConfiguration.getTimerQueueMap().put(prepareTestCase.getName(),new ConcurrentLinkedQueue<String>());
		}
		TestScript clearupTestCase=testSuite.getClearupTestCase();
		if(clearupTestCase!=null){
			TestConfiguration.getTimerQueueMap().put(clearupTestCase.getName(),new ConcurrentLinkedQueue<String>());
		}
		Iterator<TestCase> testCaseIterator = testSuite.getTestCaseList().iterator();
		while (testCaseIterator.hasNext()) {
			TestConfiguration.getTimerQueueMap().put(((TestCase) testCaseIterator.next()).getTestScript().getName(),
					new ConcurrentLinkedQueue<String>());
		}
		TimerWriter timerWriter = new TimerWriter();
		timerWriter.start();

		for(String userName: TestConfiguration.getUserNames()){
			TestConfiguration.getUserLoggerQueueMap().put(userName, new ConcurrentLinkedQueue<String>());
		}
		UserLoggerWriter loggerWriter=new UserLoggerWriter();
		loggerWriter.start();		
		
		int duration=TestConfiguration.getDuration();
		// Thread(VU) start
		int threadCount = TestConfiguration.getVusers();
		int i = 0;
		Thread[] thread = new Thread[threadCount];
		while (i < threadCount) {
			try {
				thread[i] = new TestThread(testSuite);
				thread[i].start();
				if(duration>0){
					thread[i].join(duration);
				}else{
					thread[i].join();
				}
			} catch (InterruptedException e) {
				TestConfiguration.getCommonLogger().write(
						"TestEntry - runTest() Test thread can't start normally! ",
						Level.ERROR);
				e.printStackTrace();
				break;
			}
			i++;
		}
		
		//stop log write thread
		timerWriter.setStopSignal(true);
		loggerWriter.setStopSignal(true);
		//close common log file
		TestConfiguration.getCommonLogger().flush(true);
	}
}
