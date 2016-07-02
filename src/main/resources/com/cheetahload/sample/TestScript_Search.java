package main.resources.com.cheetahload.sample;

import main.resources.com.cheetahload.TestScript;
import main.resources.com.cheetahload.log.Level;
import main.resources.com.cheetahload.log.Logger;
import main.resources.com.cheetahload.log.LoggerName;

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
