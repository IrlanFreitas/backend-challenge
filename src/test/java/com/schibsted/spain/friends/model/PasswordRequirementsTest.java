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

}