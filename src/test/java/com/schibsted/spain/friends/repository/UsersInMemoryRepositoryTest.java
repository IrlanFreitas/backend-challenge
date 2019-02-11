package com.schibsted.spain.friends.repository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UsersInMemoryRepositoryTest {

	private final UsersInMemoryRepository usersRepository = new UsersInMemoryRepository();
	private final String password = "123456";
	private final String user = "user";

	@Before
	public void setUp() {
		usersRepository.save(user, password);
	}

	@Test
	public void shouldReturnFalseWhenUserDoesntExist() {
		assertFalse(usersRepository.userExists("notExisting"));
	}

	@Test
	public void shouldReturnTrueWhenUserIsSaved() {
		assertTrue(usersRepository.userExists(user));
	}

	@Test
	public void shouldReturnNullWhenThereIsNoUser() {
		assertNull(usersRepository.getPassword("notExisting"));
	}

	@Test
	public void shouldReturnPasswordTrueWhenUserIsSaved() {
		String actual = usersRepository.getPassword(user);

		assertEquals(password, actual);
	}
}
