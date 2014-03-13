package com.cheetahload.sample;

import com.cheetahload.TestScript;
import com.cheetahload.log.Level;

public class TestCase_Login extends TestScript {
	public TestCase_Login() {
		super();
	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		getUserLogger().write(this.getName() + " - test()", Level.INFO);
	}

	@Override
	public void clearup() {
		// TODO Auto-generated method stub
	}

}
