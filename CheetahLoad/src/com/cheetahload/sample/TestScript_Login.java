package com.cheetahload.sample;

import com.cheetahload.TestScript;
import com.cheetahload.log.Level;

public class TestScript_Login extends TestScript {

	@Override
	public void prepare() throws Exception{
	

	}

	@Override
	public void test() throws Exception {
		
		getUserLogger().write(this.getName() + " - test()", Level.INFO);
			Thread.sleep(10);
	}

	@Override
	public void clearup() throws Exception {
		
	}

}
