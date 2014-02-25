package com.cheetahload;

import java.util.Vector;

public class VirtualUser {
	public static Vector<String> generateUserNameVector(String prefix, int digit, int startNumber, int userNumber) {
		Vector<String> userNameVector = new Vector<String>();
		if (prefix == null) {
			// Todo deal with the exception
			return userNameVector;
		}
		// sample: digit=4, startNumber=10, then result is 0010,0011,0012
		if (digit <= 0) {
			// Todo deal with the exception
			return userNameVector;
		}
		if (String.valueOf(startNumber).length() > digit) {
			// Todo deal with the exception
			return userNameVector;
		}
		if (userNumber <= 0) {
			// Todo deal with the exception
			return userNameVector;
		}
		for (int i = startNumber; i < userNumber; i++) {
			userNameVector.add(prefix + String.format("%0" + digit + "d", String.valueOf(i)));
		}
		return userNameVector;
	}
}
