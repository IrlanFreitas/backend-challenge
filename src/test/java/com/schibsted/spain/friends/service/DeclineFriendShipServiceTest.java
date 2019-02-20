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

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.collections.Sets.newSet;

@RunWith(MockitoJUnitRunner.class)
public class DeclineFriendShipServiceTest {

	@Mock
	private PasswordsRepository passwordsRepository;

	@Mock
	private FriendsRepository friendsRepository;

	@Mock
	private RequestsRepository requestsRepository;

	private FriendShipService friendShipService;
	private final Password password = new Password("passWord123");
	private final User notExisting = new User("notExists");
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
		friendShipService.decline(notExisting, password, pepe);
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowExpectedWhenToUserDoesntExists() {
		friendShipService.decline(pepe, password, notExisting);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowExpectedWhenPasswordIsNotCorrect() {
		final Password wrong = new Password("wrongPass");
		friendShipService.decline(pepe, wrong, juan);
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowExceptionIfUserHasNoRequest() {
		friendShipService.decline(pepe, password, juan);
	}

	@Test
	public void shouldCallMethodAddAsFriend() {
		final Set<User> juanRequests = newSet(pepe);
		final Set<User> pepeRequests = newSet(juan);

		when(requestsRepository.getFriendShipRequests(juan)).thenReturn(juanRequests);
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(pepeRequests);

		friendShipService.decline(pepe, password, juan);

		juanRequests.remove(pepe);
		pepeRequests.remove(juan);

		verify(requestsRepository, times(1)).addRequest(juan, juanRequests);
		verify(requestsRepository, times(1)).addRequest(pepe, pepeRequests);
	}
}
