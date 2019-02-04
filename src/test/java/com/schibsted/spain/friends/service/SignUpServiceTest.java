package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Username;
import com.schibsted.spain.friends.repository.LoginRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

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

		loginService.saveUser(existingUser);
	}
}
