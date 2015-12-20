package com.cheetahload.sample;

import com.cheetahload.TestScript;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;

public class TestScript_Search extends TestScript {

	@Override
	public void test() throws Exception {
		
		Logger.get(LoggerName.User).write(this.getName() + " - test()", Level.INFO);
		Thread.sleep(20);
		throw new Exception("aaa");
		
	}

	@Override
	public void prepare() throws Exception {
		
	}

	@Override
	public void clearup() throws Exception {
		
	}

}
