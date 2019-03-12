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

import static java.util.Collections.emptySet;
import static java.util.Optional.of;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.collections.Sets.newSet;

@RunWith(MockitoJUnitRunner.class)
public class AcceptFriendShipServiceTest {

	private final User pepe = new User("Pepito");
	private final User juan = new User("Juanito");
	private final User notExisting = new User("notExists");
	private final Password password = new Password("passWord123");

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
		when(usersRepository.getPassword(pepe)).thenReturn(of(password));
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowExpectedWhenFromUserDoesntExists() {
		friendShipService.accept(notExisting, password, pepe);
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowExpectedWhenToUserDoesntExists() {
		friendShipService.accept(pepe, password, notExisting);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowExpectedWhenPasswordIsNotCorrect() {
		final Password wrong = new Password("wrongPass");
		friendShipService.accept(pepe, wrong, juan);
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowExceptionIfUserHasNoRequest() {
		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenAreFriends() {
		when(friendsRepository.getFriends(juan)).thenReturn(newLinkedHashSet(pepe));

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenAreFriendsTo() {
		when(friendsRepository.getFriends(pepe)).thenReturn(new LinkedHashSet<>());
		when(friendsRepository.getFriends(juan)).thenReturn(newLinkedHashSet(pepe));

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = NotFoundException.class)
	public void shouldFailWhenThereIsNoRequestFrom() {
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(emptySet());

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = NotFoundException.class)
	public void shouldFailWhenThereIsNoRequestTo() {
		when(requestsRepository.getFriendShipRequests(juan)).thenReturn(emptySet());
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(newSet(juan));

		friendShipService.accept(pepe, password, juan);
	}

	@Test
	public void shouldCallMethodAddAsFriend() {

		when(friendsRepository.getFriends(juan)).thenReturn(newLinkedHashSet(new User("Margarita")));
		when(friendsRepository.getFriends(pepe)).thenReturn(newLinkedHashSet());
		when(requestsRepository.getFriendShipRequests(juan)).thenReturn(newSet(pepe));
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(newSet(juan));

		friendShipService.accept(pepe, password, juan);

		final var juanFriendsUpdated = newLinkedHashSet(new User("Margarita"), pepe);

		verify(friendsRepository, times(1)).addFriends(juan, juanFriendsUpdated);
		verify(friendsRepository, times(1)).addFriends(pepe, newLinkedHashSet(juan));
		verify(requestsRepository, times(1)).addRequests(juan, emptySet());
		verify(requestsRepository, times(1)).addRequests(pepe, emptySet());
	}
}
