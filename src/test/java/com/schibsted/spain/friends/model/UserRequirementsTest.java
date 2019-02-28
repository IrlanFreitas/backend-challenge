package com.schibsted.spain.friends.model;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserRequirementsTest {

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenUserNameIsNull() {
		new User(null);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenUserNameIsEmpty() {
		new User("");
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenPassHasLeftThan5Char() {
		new User("1234");
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenPassHasMoreThan10Char() {
		new User("123456789ab");
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenPassHasSpecialCharacters() {
		new User("123456-7");
	}

	@Test
	public void shouldReturnExpectedString() {
		final var expected = "Pepito12";
		final var actual = new User("Pepito12").getName();

		assertEquals(expected, actual);
	}
}
