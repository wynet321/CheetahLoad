package main.resources.com.cheetahload.sample;

import main.resources.com.cheetahload.TestScript;
import main.resources.com.cheetahload.log.Level;
import main.resources.com.cheetahload.timer.Transaction;

public class TestScript_Login extends TestScript {

	@Override
	public void prepare() throws Exception {

	}

	@Override
	public void test() throws Exception {
		logger.add(this.getName() + " - test()", Level.INFO);
		Transaction tranx = new Transaction(this.getName(),"tranx2");
		tranx.begin();
		Thread.sleep(10);
		tranx.end();
	}

	@Override
	public void clearup() throws Exception {

	}

}
