package com.schibsted.spain.friends.model;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class Username {
	private final String username;

	public Username(String username) {
		checkRestrictions(username);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Username username1 = (Username) o;
		return username.equals(username1.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
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
