package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import com.schibsted.spain.friends.exceptions.NotFoundException;
import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.FriendsRepository;
import com.schibsted.spain.friends.repository.UsersRepository;
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
	private UsersRepository usersRepository;

	@Mock
	private FriendsRepository friendsRepository;

	@Mock
	private RequestsRepository requestsRepository;

	private FriendShipService friendShipService;
	private final User pepe = new User("Pepito");
	private final User juan = new User("Juanito");
	private final User notExisting = new User("notExists");
	private final Password pepePassword = new Password("passWord123");

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository, friendsRepository, requestsRepository);
		when(usersRepository.userExists(notExisting)).thenReturn(false);
		when(usersRepository.userExists(juan)).thenReturn(true);
		when(usersRepository.userExists(pepe)).thenReturn(true);
		when(usersRepository.getPassword(pepe)).thenReturn(pepePassword);
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowExpectedWhenFromUserDoesntExists() {
		friendShipService.request(notExisting, pepePassword, pepe);
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowExpectedWhenToUserDoesntExists() {
		friendShipService.request(pepe, pepePassword, notExisting);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowExpectedWhenPasswordIsNotCorrect() {
		final Password wrong = new Password("wrongPass");
		friendShipService.request(pepe, wrong, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenRequestToHimself() {
		friendShipService.request(pepe, pepePassword, pepe);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowExpectedWhenUsersAreInRequest() {
		final Set<User> pepeRequests = newSet(juan);

		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(pepeRequests);

		friendShipService.request(pepe, pepePassword, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenRequestAreFriends() {
		final LinkedHashSet<User> pepeFriends = newLinkedHashSet(juan);

		when(friendsRepository.getFriends(pepe)).thenReturn(pepeFriends);

		friendShipService.request(pepe, pepePassword, juan);
	}

	@Test
	public void shouldWorksWithNoRequestsReturn() {
		final Set<User> pepeRequests = newSet(juan);
		final Set<User> juanRequests = newSet(pepe);

		friendShipService.request(pepe, pepePassword, juan);

		verify(requestsRepository, times(1)).addRequest(pepe, pepeRequests);
		verify(requestsRepository, times(1)).addRequest(juan, juanRequests);
	}

}
