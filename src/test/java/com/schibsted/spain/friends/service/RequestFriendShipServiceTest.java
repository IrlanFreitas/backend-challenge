package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import com.schibsted.spain.friends.exceptions.NotFoundException;
import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.FriendsRepository;
import com.schibsted.spain.friends.repository.PasswordsRepository;
import com.schibsted.spain.friends.repository.RequestsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.collections.Sets.newSet;

@RunWith(MockitoJUnitRunner.class)
public class RequestFriendShipServiceTest {

	@Mock
	private PasswordsRepository passwordsRepository;

	@Mock
	private FriendsRepository friendsRepository;

	@Mock
	private RequestsRepository requestsRepository;

	private FriendShipService friendShipService;
	private final User notExisting = new User("notExists");
	private final Password password = new Password("passWord123");
	private final User pepe = new User("Pepito");
	private final User juan = new User("Juanito");

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(passwordsRepository, friendsRepository, requestsRepository);
		when(passwordsRepository.userExists(notExisting)).thenReturn(false);
		when(passwordsRepository.getPassword(pepe)).thenReturn(password);
		when(passwordsRepository.userExists(pepe)).thenReturn(true);
		when(passwordsRepository.userExists(juan)).thenReturn(true);
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowExpectedWhenFromUserDoesntExists() {
		friendShipService.request(notExisting, password, pepe);
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowExpectedWhenToUserDoesntExists() {
		friendShipService.request(pepe, password, notExisting);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowExpectedWhenPasswordIsNotCorrect() {
		final Password wrong = new Password("wrongPass");
		friendShipService.request(pepe, wrong, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowExpectedWhenUsersAreInRequest() {
		final Set<User> pepeRequests = newSet(juan);

		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(pepeRequests);

		friendShipService.request(pepe, password, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenRequestAreFriends() {
		final LinkedHashSet<User> pepeFriends = newLinkedHashSet(juan);

		when(friendsRepository.getFriends(pepe)).thenReturn(pepeFriends);

		friendShipService.request(pepe, password, juan);
	}

	@Test
	public void shouldWorksWithNoRequestsReturn() {
		final Set<User> pepeRequests = newSet(juan);
		final Set<User> juanRequests = newSet(pepe);

		friendShipService.request(pepe, password, juan);

		verify(requestsRepository, times(1)).addRequest(pepe, pepeRequests);
		verify(requestsRepository, times(1)).addRequest(juan, juanRequests);
	}

}
