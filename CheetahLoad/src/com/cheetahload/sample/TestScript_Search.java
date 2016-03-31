package com.cheetahload.sample;

import com.cheetahload.TestScript;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;

public class TestScript_Search extends TestScript {

	@Override
	public void test() throws Exception {
		Logger.get(LoggerName.User).add(this.getName() + " - test()", Level.INFO);
		Thread.sleep(20);
		// negative test
		String nullString = null;
		nullString.substring(1);
	}

	@Override
	public void prepare() throws Exception {

	}

	@Override
	public void clearup() throws Exception {

	}

}
