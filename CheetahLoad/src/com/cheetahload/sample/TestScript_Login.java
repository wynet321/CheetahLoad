package com.cheetahload.sample;

import com.cheetahload.TestScript;
import com.cheetahload.log.LogLevel;

public class TestScript_Login extends TestScript {

	@Override
	public void prepare() throws Exception{
		// TODO Auto-generated method stub

	}

	@Override
	public void test() throws Exception {
		// TODO Auto-generated method stub
		getUserLogger().write(this.getName() + " - test()", LogLevel.INFO);
			Thread.sleep(1);
	}

	@Override
	public void clearup() throws Exception {
		// TODO Auto-generated method stub
	}

}
