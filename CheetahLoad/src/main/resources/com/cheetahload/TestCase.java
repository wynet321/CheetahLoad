package main.resources.com.cheetahload;

public final class TestCase {

	private TestScript testScript;
	private int percentage;

	public TestCase(TestScript testScript, int percentage) {
		if (testScript != null) {
			this.testScript = testScript;
		}
		if (percentage >= 0) {
			this.percentage = percentage;
		}
	}

	public final TestScript getTestScript() {
		return testScript;
	}

	public final int getPercentage() {
		return percentage;
	}
}
