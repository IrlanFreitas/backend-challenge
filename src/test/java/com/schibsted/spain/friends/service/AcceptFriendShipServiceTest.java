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

import static java.util.Collections.emptySet;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.collections.Sets.newSet;

@RunWith(MockitoJUnitRunner.class)
public class AcceptFriendShipServiceTest {

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
	private final Password password = new Password("passWord123");

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository, friendsRepository, requestsRepository);
		when(usersRepository.userExists(notExisting)).thenReturn(false);
		when(usersRepository.userExists(juan)).thenReturn(true);
		when(usersRepository.userExists(pepe)).thenReturn(true);
		when(usersRepository.getPassword(pepe)).thenReturn(password);
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
		final LinkedHashSet<User> friends = newLinkedHashSet(pepe);

		when(friendsRepository.getFriends(juan)).thenReturn(friends);

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenAreFriendsTo() {
		final LinkedHashSet<User> juanFriends = newLinkedHashSet(pepe);

		when(friendsRepository.getFriends(pepe)).thenReturn(new LinkedHashSet<>());
		when(friendsRepository.getFriends(juan)).thenReturn(juanFriends);

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = NotFoundException.class)
	public void shouldFailWhenThereIsNoRequestFrom() {
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(emptySet());

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = NotFoundException.class)
	public void shouldFailWhenThereIsNoRequestTo() {
		final Set<User> pepeRequest = newSet(juan);

		when(requestsRepository.getFriendShipRequests(juan)).thenReturn(emptySet());
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(pepeRequest);

		friendShipService.accept(pepe, password, juan);
	}

	@Test
	public void shouldCallMethodAddAsFriend() {
		final LinkedHashSet<User> juanFriends = newLinkedHashSet(new User("Margarita"));
		final Set<User> juanRequests = newSet(pepe);
		final LinkedHashSet<User> pepeFriends = new LinkedHashSet<>();
		final Set<User> pepeRequests = newSet(juan);

		when(requestsRepository.getFriendShipRequests(juan)).thenReturn(juanRequests);
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(pepeRequests);

		when(friendsRepository.getFriends(juan)).thenReturn(juanFriends);
		when(friendsRepository.getFriends(pepe)).thenReturn((pepeFriends));

		friendShipService.accept(pepe, password, juan);

		juanFriends.add(pepe);
		pepeFriends.add(juan);
		juanRequests.remove(pepe);
		pepeRequests.remove(juan);

		verify(friendsRepository, times(1)).addAsFriends(juan, juanFriends);
		verify(friendsRepository, times(1)).addAsFriends(pepe, pepeFriends);

		verify(requestsRepository, times(1)).addRequest(juan, juanRequests);
		verify(requestsRepository, times(1)).addRequest(pepe, pepeRequests);
	}
}
