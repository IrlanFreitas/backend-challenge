package com.schibsted.spain.friends.model;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class Password {

	private final String password;

	public Password(String password) {
		checkRestrictions(password);

		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Password password1 = (Password) o;
		return password.equals(password1.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(password);
	}

	private void checkRestrictions(String password) {
		throwExceptionIf(password == null);

		throwExceptionIf(password.isEmpty());

		throwExceptionIf(password.length() < 8);

		throwExceptionIf(password.length() > 12);

		throwExceptionIf(compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE).matcher(password).find());
	}

	private void throwExceptionIf(boolean b) {
		if (b) {
			throw new IllegalArgumentException();
		}
	}

}
