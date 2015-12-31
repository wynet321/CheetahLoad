package com.cheetahload;

import java.util.ArrayList;
import java.util.List;

public class VirtualUser {
	public static List<String> generateUserNames(String prefix, int digit, int startNumber, int userNumber) {
		List<String> userNames = new ArrayList<String>();
		prefix = (prefix == null) ? "" : prefix;
		digit = (digit <= 0) ? 1 : digit;
		startNumber = (startNumber < 0) ? 0 : startNumber;
		startNumber = (String.valueOf(startNumber).length() > digit) ? 0 : startNumber;
		userNumber = (userNumber <= 0) ? 1 : userNumber;
		// sample: digit=4, startNumber=10, then result is 0010,0011,0012
		for (int i = startNumber; i < startNumber + userNumber; i++) {
			userNames.add(prefix + String.format("%0" + digit + "d", i));
		}
		return userNames;
	}
}
