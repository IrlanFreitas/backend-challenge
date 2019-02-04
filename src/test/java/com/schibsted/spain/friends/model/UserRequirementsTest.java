package com.schibsted.spain.friends.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserRequirementsTest {

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenUserNameIsNull() {
		new Username(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenUserNameIsEmpty() {
		new Username("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassHasLeftThan5Char() {
		new Username("1234");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassHasMoreThan10Char() {
		new Username("123456789ab");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPassHasSpecialCharacters() {
		new Username("123456-7");
	}

	@Test
	public void shouldReturnExpectedString() {
		String expected = "Pepito12";
		String actual = new Username("Pepito12").getUsername();

		assertEquals(expected, actual);
	}

}
