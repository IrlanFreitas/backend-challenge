package com.schibsted.spain.friends.model;

import org.junit.Test;

public class PasswordRequirementsTest {

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassWordIsNull() {
		new Password(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassWordIsEmpty() {
		new Password("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassHasLeftThan8Char() {
		new Password("123456a");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassHasMoreThan12Char() {
		new Password("123456789aBcD");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassHasSpecialCharacters() {
		new Password("123456-789");
	}

}
