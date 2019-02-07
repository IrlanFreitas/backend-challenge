package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListFriendShipServiceTest {

	@Mock
	private UsersRepository usersRepository;

	private FriendShipService friendShipService;

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenNotExistingUser() {
		friendShipService.list(new User("NotExist"), null);
	}
}
