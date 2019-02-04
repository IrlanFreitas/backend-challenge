package com.schibsted.spain.friends.model;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

class Username {
	private final String username;

	Username(String username) {
		checkRestrictions(username);
		this.username = username;
	}

	String getUsername() {
		return username;
	}


	private void checkRestrictions(String username) {
		throwExceptionIf(username == null);

		throwExceptionIf(username.isEmpty());

		throwExceptionIf(username.length() < 5);

		throwExceptionIf(username.length() > 10);

		throwExceptionIf(compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE).matcher(username).find());
	}

	private void throwExceptionIf(boolean b) {
		if (b) {
			throw new IllegalArgumentException();
		}
	}
}
