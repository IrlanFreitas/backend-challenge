package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
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

	@Test
	public void shouldReturnEmptyListWhenHasNoFriends() {
		List<String> expected = emptyList();
		List<String> actual = friendShipService.list(pepe, password);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnExpectedWhenUserHasFriends() {
		Set<String> pepeFriends = new HashSet<>();
		pepeFriends.add("Margarita");
		pepeFriends.add("Juanito");
		when(usersRepository.getFriends(pepe.getName())).thenReturn(Optional.of(pepeFriends));

		List<String> expected = Arrays.asList("Margarita", "Juanito");
		List<String> actual = friendShipService.list(pepe, password);

		assertEquals(expected, actual);
	}
}
