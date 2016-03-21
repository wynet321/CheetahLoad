# CheetahLoad
Load test tool for Web application.
##Framework introduction
	There are 3 layers in this framework as below.
		1. TestScript
			This is the atomic transaction. Every test request class need to extends this class. 
	  		There is 3 methods which can be overrided. 
    			prepare() method includes preparation operations before each test request.
    			test() method includes test request need to be meatured.
    			clearup() method includes clear up operations after each test request.
  			The framework will record the duration of each test() method. 
		2. TestCase
	  		TestCase includes one or more TestScript instances and its percentage to execute.
		3. TestSuite
  			This is the test schedule for the whole load test, including all of the TestCases.

##Sample TestScript
public class TestScript_Login extends TestScript {

	@Override
	public void prepare() throws Exception {

	}

	@Override
	public void test() throws Exception {
			Logger.get(LoggerName.User).add(this.getName() + " - test()", Level.INFO);
			Transaction tranx = new Transaction(this.getName(),"tranx2");
			tranx.begin();
			Thread.sleep(10);
			tranx.end();
		}

		@Override
		public void clearup() throws Exception {

		}
	}

