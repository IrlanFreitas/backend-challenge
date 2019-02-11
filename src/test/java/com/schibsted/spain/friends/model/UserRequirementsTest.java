package com.schibsted.spain.friends.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserRequirementsTest {

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenUserNameIsNull() {
		new User(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenUserNameIsEmpty() {
		new User("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassHasLeftThan5Char() {
		new User("1234");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassHasMoreThan10Char() {
		new User("123456789ab");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassHasSpecialCharacters() {
		new User("123456-7");
	}

	@Test
	public void shouldReturnExpectedString() {
		final String expected = "Pepito12";
		final String actual = new User("Pepito12").getName();

		assertEquals(expected, actual);
	}

}
