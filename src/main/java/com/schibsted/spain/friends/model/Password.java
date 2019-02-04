package com.schibsted.spain.friends.model;

import java.util.regex.Pattern;

class Password {

	private String password;

	Password(String password) {
		if (password == null) {
			throw new IllegalArgumentException();
		}

		if (password.isEmpty()) {
			throw new IllegalArgumentException();
		}

		if (password.length() < 8) {
			throw new IllegalArgumentException();
		}

		if (password.length() > 12) {
			throw new IllegalArgumentException();
		}

		Pattern p = Pattern.compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE);

		if (p.matcher(password).find()) {
			throw new IllegalArgumentException();
		}

		this.password = password;
	}

	String getPassword() {
		return password;
	}
}
