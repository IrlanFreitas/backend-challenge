package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.Username;
import com.schibsted.spain.friends.repository.LoginRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SignUpServiceTest {

	@Mock
	private LoginRepository loginRepository;

	private LoginService loginService;

	@Before
	public void setUp() {
		loginService = new LoginService(loginRepository);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenUserExists() {
		Username existingUser = new Username("Existing");

		when(loginRepository.userExists(existingUser.getUsername())).thenReturn(true);

		loginService.saveUser(existingUser, null);
	}

	@Test
	public void shouldReturnTrueWhenUserDoesntExist() {
		Username user = new Username("user123");
		Password password = new Password("12345678ab");

		when(loginRepository.userExists(user.getUsername())).thenReturn(false);

		assertTrue(loginService.saveUser(user, password));
		verify(loginRepository, times(1)).saveUser(user.getUsername(), password.getPassword());
	}
}
