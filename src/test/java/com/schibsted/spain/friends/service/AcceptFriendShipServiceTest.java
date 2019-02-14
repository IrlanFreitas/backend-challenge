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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AcceptFriendShipServiceTest {

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
		final Set<User> friends = newLinkedHashSet(pepe);

		when(friendsRepository.getFriends(juan)).thenReturn(Optional.of((friends)));

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = BadRequestException.class)
	public void shouldFailWhenAreFriendsTo() {
		final Set<User> juanFriends = newLinkedHashSet(pepe);

		when(friendsRepository.getFriends(pepe)).thenReturn(Optional.of((emptySet())));
		when(friendsRepository.getFriends(juan)).thenReturn(Optional.of((juanFriends)));

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = NotFoundException.class)
	public void shouldFailWhenThereIsNoRequestFrom() {
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(Optional.of(emptySet()));

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = NotFoundException.class)
	public void shouldFailWhenThereIsNoRequestTo() {
		final Set<User> pepeRequest = new HashSet<>();
		pepeRequest.add(juan);

		when(requestsRepository.getFriendShipRequests(juan)).thenReturn(Optional.of(emptySet()));
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(Optional.of(pepeRequest));

		friendShipService.accept(pepe, password, juan);
	}

	@Test
	public void shouldCallMethodAddAsFriend() {
		final LinkedHashSet<User> juanFriends = newLinkedHashSet(new User("Margarita"));
		final Set<User> juanRequests = newLinkedHashSet(pepe);
		final LinkedHashSet<User> pepeFriends = new LinkedHashSet<>();
		final Set<User> pepeRequests = newLinkedHashSet(juan);

		when(requestsRepository.getFriendShipRequests(juan)).thenReturn(Optional.of(juanRequests));
		when(requestsRepository.getFriendShipRequests(pepe)).thenReturn(Optional.of(pepeRequests));

		when(friendsRepository.getFriends(juan)).thenReturn(Optional.of((juanFriends)));
		when(friendsRepository.getFriends(pepe)).thenReturn(Optional.of((pepeFriends)));

		friendShipService.accept(pepe, password, juan);

		juanFriends.add(pepe);
		verify(friendsRepository, times(1)).addAsFriends(juan, juanFriends);
		pepeFriends.add(juan);
		verify(friendsRepository, times(1)).addAsFriends(pepe, pepeFriends);
		juanRequests.remove(pepe);
		verify(requestsRepository, times(1)).addRequest(juan, juanRequests);
		pepeRequests.remove(juan);
		verify(requestsRepository, times(1)).addRequest(pepe, pepeRequests);
	}
}
