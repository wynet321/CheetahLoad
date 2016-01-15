package com.cheetahload.sample;

import com.cheetahload.TestScript;
import com.cheetahload.log.Level;
import com.cheetahload.log.Logger;
import com.cheetahload.log.LoggerName;
import com.cheetahload.timer.Transaction;

public class TestScript_Login extends TestScript {

	@Override
	public void prepare() throws Exception {

	}

	@Override
	public void test() throws Exception {
		Logger.get(LoggerName.User).write(this.getName() + " - test()", Level.INFO);
		Transaction tranx = new Transaction(this.getName(),"tranx2");
		tranx.begin();
		Thread.sleep(10);
		tranx.end();
	}

	@Override
	public void clearup() throws Exception {

	}

}
