package com.schibsted.spain.friends.model;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class User {
	private final String name;

	public User(String name) {
		checkRestrictions(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user1 = (User) o;
		return name.equals(user1.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	private void checkRestrictions(String name) {
		throwExceptionIf(name == null);

		throwExceptionIf(name.isEmpty());

		throwExceptionIf(name.length() < 5);

		throwExceptionIf(name.length() > 10);

		throwExceptionIf(compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE).matcher(name).find());
	}

	private void throwExceptionIf(boolean b) {
		if (b) {
			throw new IllegalArgumentException();
		}
	}
}
