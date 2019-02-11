package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
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
	private UsersRepository usersRepository;

	private FriendShipService friendShipService;
	private final User notExisting = new User("notExists");
	private final Password password = new Password("passWord123");
	private final User pepe = new User("Pepito");
	private final User juan = new User("Juanito");

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository);
		when(usersRepository.userExists(notExisting.getName())).thenReturn(false);
		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());
		when(usersRepository.userExists(pepe.getName())).thenReturn(true);
		when(usersRepository.userExists(juan.getName())).thenReturn(true);
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
		final Set<String> pepeRequests = new HashSet<>();
		pepeRequests.add(juan.getName());

		when(usersRepository.getFriendShipRequests(pepe.getName())).thenReturn(Optional.of(pepeRequests));

		friendShipService.request(pepe, password, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenRequestAreFriends() {
		final Set<String> pepeFriends = new HashSet<>();
		pepeFriends.add(juan.getName());

		when(usersRepository.getFriends(pepe.getName())).thenReturn(Optional.of(pepeFriends));

		friendShipService.request(pepe, password, juan);
	}

	@Test
	public void shouldWorksWithNoRequestsReturn() {
		final Set<String> pepeRequests = new HashSet<>();
		pepeRequests.add(juan.getName());

		Set<String> juanRequests = new HashSet<>();
		juanRequests.add(pepe.getName());

		friendShipService.request(pepe, password, juan);

		verify(usersRepository, times(1)).addRequest(pepe.getName(), pepeRequests);
		verify(usersRepository, times(1)).addRequest(juan.getName(), juanRequests);
	}

}
