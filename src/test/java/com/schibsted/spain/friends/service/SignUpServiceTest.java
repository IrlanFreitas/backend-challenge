package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SignUpServiceTest {

	@Mock
	private UsersRepository usersRepository;

	private SignUpService signUpService;

	@Before
	public void setUp() {
		signUpService = new SignUpService(usersRepository);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenUserExists() {
		User existingUser = new User("Existing");

		when(usersRepository.exists(existingUser.getName())).thenReturn(true);

		signUpService.saveUser(existingUser, null);
	}

	@Test
	public void shouldSaveUserWhenUserDoesntExist() {
		User user = new User("user123");
		Password password = new Password("12345678ab");

		when(usersRepository.exists(user.getName())).thenReturn(false);

		signUpService.saveUser(user, password);

		verify(usersRepository, times(1)).save(user.getName(), password.getPassword());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenLoginUserDoesntExist() {
		User user = new User("user123");
		Password password = new Password("12345678ab");

		when(usersRepository.exists(user.getName())).thenReturn(true);

		signUpService.checkLogin(user, password);
	}

	@Test
	public void shouldReturnFalseWhenPasswordsDontMatch() {
		User user = new User("user123");
		Password password = new Password("12345678ab");

		when(usersRepository.exists(user.getName())).thenReturn(false);

		when(usersRepository.getPassword(user.getName())).thenReturn("abcd12345");

		assertFalse(signUpService.checkLogin(user, password));
	}

	@Test
	public void shouldReturnTrueWhenPasswordsMatch() {
		User user = new User("user123");
		Password password = new Password("12345678ab");

		when(usersRepository.exists(user.getName())).thenReturn(false);

		when(usersRepository.getPassword(user.getName())).thenReturn(password.getPassword());

		assertTrue(signUpService.checkLogin(user, password));
	}
}
