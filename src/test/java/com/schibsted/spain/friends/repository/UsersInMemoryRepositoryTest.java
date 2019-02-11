package com.schibsted.spain.friends.repository;

import org.junit.Before;

public class UsersInMemoryRepositoryTest {

	private final UsersInMemoryRepository usersInMemoryRepository = new UsersInMemoryRepository();
	private final String password = "123456";
	private final String user = "user";

	@Before
	public void setUp() {
		usersInMemoryRepository.save(user, password);
	}
}
