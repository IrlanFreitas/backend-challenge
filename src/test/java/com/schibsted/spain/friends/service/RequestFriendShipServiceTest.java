package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersInMemoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestFriendShipServiceTest {

	@Mock
	private UsersInMemoryRepository usersRepository;

	private FriendShipService friendShipService;
	private final User notExisting = new User("notExists");
	private final Password password = new Password("passWord123");
	private final User pepe = new User("Pepito");
	private final User juan = new User("Juanito");

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository);
		when(usersRepository.userExists(notExisting)).thenReturn(false);
		when(usersRepository.getPassword(pepe)).thenReturn(password);
		when(usersRepository.userExists(pepe)).thenReturn(true);
		when(usersRepository.userExists(juan)).thenReturn(true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenFromUserDoesntExists() {
		friendShipService.request(notExisting, password, pepe);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenToUserDoesntExists() {
		friendShipService.request(pepe, password, notExisting);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenPasswordIsNotCorrect() {
		final Password wrong = new Password("wrongPass");
		friendShipService.request(pepe, wrong, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenUsersAreInRequest() {
		final Set<User> pepeRequests = new HashSet<>();
		pepeRequests.add(juan);

		when(usersRepository.getFriendShipRequests(pepe)).thenReturn(Optional.of(pepeRequests));

		friendShipService.request(pepe, password, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenRequestAreFriends() {
		final Set<User> pepeFriends = new HashSet<>();
		pepeFriends.add(juan);

		when(usersRepository.getFriends(pepe)).thenReturn(Optional.of(pepeFriends));

		friendShipService.request(pepe, password, juan);
	}

	@Test
	public void shouldWorksWithNoRequestsReturn() {
		final Set<User> pepeRequests = new HashSet<>();
		pepeRequests.add(juan);

		Set<User> juanRequests = new HashSet<>();
		juanRequests.add(pepe);

		friendShipService.request(pepe, password, juan);

		verify(usersRepository, times(1)).addRequest(pepe, pepeRequests);
		verify(usersRepository, times(1)).addRequest(juan, juanRequests);
	}

}
