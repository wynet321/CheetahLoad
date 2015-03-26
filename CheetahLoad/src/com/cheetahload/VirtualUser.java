package com.cheetahload;

import java.util.Vector;

public class VirtualUser {
	public static Vector<String> generateUserNames(String prefix, int digit, int startNumber, int userNumber) {
		Vector<String> userNameVector = new Vector<String>();
		if (prefix == null) {
			// TODO: deal with the exception
			return userNameVector;
		}
		// sample: digit=4, startNumber=10, then result is 0010,0011,0012
		if (digit <= 0) {
			// TODO deal with the exception
			return userNameVector;
		}
		if (String.valueOf(startNumber).length() > digit) {
			// TODO deal with the exception
			return userNameVector;
		}
		if (userNumber <= 0) {
			// TODO deal with the exception
			return userNameVector;
		}
		for (int i = startNumber; i < startNumber + userNumber; i++) {
			userNameVector.add(prefix + String.format("%0" + digit + "d", i));
		}
		return userNameVector;
	}
}
