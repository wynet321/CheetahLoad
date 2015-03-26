package com.cheetahload.sample;

import com.cheetahload.TestScript;
import com.cheetahload.log.Level;

public class TestScript_Login extends TestScript {

	@Override
	public void prepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		getUserLogger().write(this.getName() + " - test()", Level.INFO);
		try{
			Thread.sleep(1);
		}catch (Exception e){
			e.printStackTrace();
			getUserLogger().write(this.getName() + " - test() failed.", Level.ERROR);
		}
	}

	@Override
	public void clearup() {
		// TODO Auto-generated method stub
	}

}
