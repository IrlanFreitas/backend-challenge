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
public class ListFriendShipServiceTest {

	@Mock
	private UsersRepository usersRepository;

	private FriendShipService friendShipService;
	private final Password password = new Password("passWord123");
	private final User pepe = new User("Pepito");

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository);
		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());
		when(usersRepository.userExists(pepe.getName())).thenReturn(true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenNotExistingUser() {
		friendShipService.list(new User("NotExist"), password);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPasswordWrong() {
		friendShipService.list(pepe, new Password("wrongPass"));
	}
}
