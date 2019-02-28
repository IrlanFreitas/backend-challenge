package com.schibsted.spain.friends.model;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import org.junit.Test;

public class PasswordRequirementsTest {

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenPassWordIsNull() {
		new Password(null);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenPassWordIsEmpty() {
		new Password("");
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenPassHasLeftThan8Char() {
		new Password("123456a");
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenPassHasMoreThan12Char() {
		new Password("123456789aBcD");
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenPassHasSpecialCharacters() {
		new Password("123456-789");
	}

//	Just for the challenge purpose, code commented is Evil.
//	I made this test to try the hash but we don't need the getter, so deleted it and kept this test commented.

	/*@Test
	public void shouldHashWithMd5Password() {
		final Password password = new Password("12345678");

		final String expected = "25d55ad283aa400af464c76d713c07ad";
		final String actual = password.getValue();

		assertEquals(expected, actual);
	}*/
}
