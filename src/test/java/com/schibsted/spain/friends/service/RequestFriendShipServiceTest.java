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

import static java.util.Optional.of;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.collections.Sets.newSet;

@RunWith(MockitoJUnitRunner.class)
public class RequestFriendShipServiceTest {

	private final User pepe = new User("Pepito");
	private final User juan = new User("Juanito");
	private final User notExisting = new User("notExists");
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
		when(usersRepository.userExists(notExisting)).thenReturn(false);
		when(usersRepository.userExists(juan)).thenReturn(true);
		when(usersRepository.userExists(pepe)).thenReturn(true);
		when(usersRepository.getPassword(pepe)).thenReturn(of(pepePassword));
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
		final var wrong = new Password("wrongPass");
		friendShipService.request(pepe, wrong, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenRequestToHimself() {
		friendShipService.request(pepe, pepePassword, pepe);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowExpectedWhenUsersAreInRequest() {
		final var pepeRequests = newSet(juan);

		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(pepeRequests);

		friendShipService.request(pepe, pepePassword, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenRequestAreFriends() {
		final var pepeFriends = newLinkedHashSet(juan);

		when(friendsRepository.getFriends(pepe)).thenReturn(pepeFriends);

		friendShipService.request(pepe, pepePassword, juan);
	}

	@Test
	public void shouldWorksWithNoRequestsReturn() {
		final var pepeRequests = newSet(juan);
		final var juanRequests = newSet(pepe);

		friendShipService.request(pepe, pepePassword, juan);

		verify(requestsRepository, times(1)).addRequests(pepe, pepeRequests);
		verify(requestsRepository, times(1)).addRequests(juan, juanRequests);
	}
}
