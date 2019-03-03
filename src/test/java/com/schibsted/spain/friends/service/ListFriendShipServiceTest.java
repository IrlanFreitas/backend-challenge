package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import com.schibsted.spain.friends.exceptions.NotFoundException;
import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.FriendsRepository;
import com.schibsted.spain.friends.repository.RequestsRepository;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedHashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListFriendShipServiceTest {

	private final User pepe = new User("Pepito");
	private final Password pepePassword = new Password("passWord123");

	private FriendShipService friendShipService;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private FriendsRepository friendsRepository;

	@Mock
	private RequestsRepository requestsRepository;

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository, friendsRepository, requestsRepository);
		when(usersRepository.userExists(pepe)).thenReturn(true);
		when(usersRepository.getPassword(pepe)).thenReturn(of(pepePassword));
	}

	@Test(expected = NotFoundException.class)
	public void shouldFailWhenNotExistingUser() {
		final User notExist = new User("NotExist");

		when(usersRepository.userExists(notExist)).thenReturn(false);

		friendShipService.list(notExist, pepePassword);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenPasswordWrong() {
		friendShipService.list(pepe, new Password("wrongPass"));
	}

	@Test
	public void shouldReturnEmptyListWhenHasNoFriends() {
		when(friendsRepository.getFriends(pepe)).thenReturn(newLinkedHashSet());

		final List<String> expected = emptyList();
		final List<String> actual = friendShipService.list(pepe, pepePassword);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldFailWhenThereIsNoOrder() {
		final LinkedHashSet<User> pepeFriends =
				newLinkedHashSet(
						new User("Margarita"),
						new User("Juanito")
				);

		when(friendsRepository.getFriends(pepe)).thenReturn(pepeFriends);

		final List<String> expected = asList("Juanito", "Margarita");
		final List<String> actual = friendShipService.list(pepe, pepePassword);

		assertNotEquals(expected, actual);
	}

	@Test
	public void shouldReturnExpectedWhenUserHasFriends() {
		final LinkedHashSet<User> pepeFriends =
				newLinkedHashSet(
						new User("Margarita"),
						new User("Juanito")
				);

		when(friendsRepository.getFriends(pepe)).thenReturn(pepeFriends);

		final List<String> expected = asList("Margarita", "Juanito");
		final List<String> actual = friendShipService.list(pepe, pepePassword);

		assertEquals(expected, actual);
	}
}
