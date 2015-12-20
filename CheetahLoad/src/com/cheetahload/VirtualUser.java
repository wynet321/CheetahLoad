package com.cheetahload;

import java.util.ArrayList;
import java.util.List;

public class VirtualUser {
	public static List<String> generateUserNames(String prefix, int digit, int startNumber, int userNumber) {
		List<String> userNames = new ArrayList<String>();
		if (prefix == null) {
			// TODO deal with the exception
			return userNames;
		}
		// sample: digit=4, startNumber=10, then result is 0010,0011,0012
		if (digit <= 0) {
			// TODO deal with the exception
			return userNames;
		}
		if (String.valueOf(startNumber).length() > digit) {
			// TODO deal with the exception
			return userNames;
		}
		if (userNumber <= 0) {
			// TODO deal with the exception
			return userNames;
		}
		for (int i = startNumber; i < startNumber + userNumber; i++) {
			userNames.add(prefix + String.format("%0" + digit + "d", i));
		}
		return userNames;
	}
}
