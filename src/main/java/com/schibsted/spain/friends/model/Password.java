package com.schibsted.spain.friends.model;

import com.schibsted.spain.friends.exceptions.BadRequestException;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

public final class Password {

	private final String password;

	public Password(String password) {
		checkRestrictions(password);

		this.password = md5DigestAsHex(password.getBytes());
	}

	public String getPassword() {
		return password;
	}

	private void checkRestrictions(String password) {
		throwExceptionIf(password == null, "Password cant be null.");

		throwExceptionIf(password.trim().isEmpty(), "Password cant be empty.");

		throwExceptionIf(password.length() < 8, "Password has to be > 8.");

		throwExceptionIf(password.length() > 12, "Password has to be < 12.");

		throwExceptionIf(compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE).matcher(password).find(),
				"Password has to be alphanumeric.");
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
		Password password1 = (Password) o;
		return password.equals(password1.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(password);
	}

}
