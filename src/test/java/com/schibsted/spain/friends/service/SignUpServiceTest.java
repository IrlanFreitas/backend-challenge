package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
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
	private UsersRepository usersRepository;

	private SignUpService signUpService;

	@Before
	public void setUp() {
		signUpService = new SignUpService(usersRepository);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenUserExists() {
		User existingUser = new User("Existing");

		when(usersRepository.userExists(existingUser.getName())).thenReturn(true);

		signUpService.saveUser(existingUser, null);
	}

	@Test
	public void shouldSaveUserWhenUserDoesntExist() {
		User user = new User("user123");
		Password password = new Password("12345678ab");

		when(usersRepository.userExists(user.getName())).thenReturn(false);

		signUpService.saveUser(user, password);

		verify(usersRepository, times(1)).save(user.getName(), password.getPassword());
	}
}
