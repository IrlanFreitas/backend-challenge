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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SignUpServiceTest {

	@Mock
	private UsersInMemoryRepository usersRepository;

	private SignUpService signUpService;

	@Before
	public void setUp() {
		signUpService = new SignUpService(usersRepository);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowExpectedWhenUserExists() {
		final User existingUser = new User("Existing");

		when(usersRepository.userExists(existingUser)).thenReturn(true);

		signUpService.saveUser(existingUser, null);
	}

	@Test
	public void shouldSaveUserWhenUserDoesntExist() {
		final User user = new User("user123");
		final Password password = new Password("12345678ab");

		when(usersRepository.userExists(user)).thenReturn(false);

		signUpService.saveUser(user, password);

		verify(usersRepository, times(1)).save(user, password);
	}
}
