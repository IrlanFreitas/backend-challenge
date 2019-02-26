package com.schibsted.spain.friends.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashPasswordTest {

	@Test
	public void shouldHashWithMd5Password() {
		final Password password = new Password("12345678");

		final String expected = "25d55ad283aa400af464c76d713c07ad";
		final String actual = password.getPassword();

		assertEquals(expected, actual);
	}
}