package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersInMemoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SignUpServiceTest {

	private SignUpService signUpService;

	@Mock
	private UsersInMemoryRepository usersRepository;

	@Mock
	private UsersInMemoryRepository friendShipRepository;

	@Mock
	private UsersInMemoryRepository requestRepository;

	@Before
	public void setUp() {
		signUpService = new SignUpService(usersRepository, friendShipRepository, requestRepository);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowExpectedWhenUserExists() {
		final var user = new User("Existing");

		when(usersRepository.userExists(user)).thenReturn(true);

		signUpService.saveUser(user, null);
	}

	@Test
	public void shouldSaveUserWhenUserDoesntExist() {
		final var user = new User("user123");
		final var password = new Password("12345678ab");

		when(usersRepository.userExists(user)).thenReturn(false);

		signUpService.saveUser(user, password);

		verify(usersRepository, times(1)).save(user, password);
		verify(requestRepository, times(1)).addRequests(user, new HashSet<>());
		verify(friendShipRepository, times(1)).addFriends(user, new LinkedHashSet<>());
	}
}
