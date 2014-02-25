package com.cheetahload;

import java.util.Vector;

public class TestConfiguration {

	private static int duration;
	private static int loops;
	private static int vusers;
	private static Vector<String> userNameVector;
	private static String password;
	private static int thinkTime;
	private static String testSuiteName;

	public TestConfiguration() {
		// Todo get configuration from XML.

		userNameVector = VirtualUser.generateUserNameVector(prefix, digit, startNumber, userNumber);
		
		
	}

	public Vector<String> getUserNameVector() {
		return userNameVector;
	}
}
