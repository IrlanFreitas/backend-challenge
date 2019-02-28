package com.schibsted.spain.friends.model;

import com.schibsted.spain.friends.exceptions.BadRequestException;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public final class User {

	private final String name;

	public User(String name) {
		checkRestrictions(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private void checkRestrictions(String name) {
		throwExceptionIf(name == null, "Username cant be null.");

		throwExceptionIf(name.trim().isEmpty(), "Username cant be empty.");

		throwExceptionIf(name.length() < 5, "Username has to be > 8.");

		throwExceptionIf(name.length() > 10, "Username has to be < 12.");

		throwExceptionIf(compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE).matcher(name).find(),
				"Username has to be alphanumeric.");
	}

	private void throwExceptionIf(boolean condition, String cause) {
		if (condition) {
			throw new BadRequestException(cause);
		}
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
}
