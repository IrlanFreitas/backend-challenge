package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FriendShipServiceTest {

	@Mock
	private UsersRepository usersRepository;

	private FriendShipService friendShipService;

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenFromUserDoesntExists() {
		User notExisting = new User("notExisting");
		Password password = new Password("passWord123");
		User existingUser = new User("Existing");

		when(usersRepository.userExists(existingUser.getName())).thenReturn(false);

		friendShipService.request(existingUser, password, notExisting);
	}
}
