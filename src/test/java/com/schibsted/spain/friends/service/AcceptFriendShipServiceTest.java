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
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AcceptFriendShipServiceTest {

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
		when(usersRepository.userExists(notExisting.getName())).thenReturn(false);
		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());
		when(usersRepository.userExists(pepe.getName())).thenReturn(true);
		when(usersRepository.userExists(juan.getName())).thenReturn(true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenFromUserDoesntExists() {
		friendShipService.accept(notExisting, password, pepe);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenToUserDoesntExists() {
		friendShipService.accept(pepe, password, notExisting);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenPasswordIsNotCorrect() {
		final Password wrong = new Password("wrongPass");
		friendShipService.accept(pepe, wrong, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfUserHasNoRequest() {
		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenAreFriends() {
		final Set<String> friends = newLinkedHashSet(pepe.getName());

		when(usersRepository.getFriends(juan.getName())).thenReturn(Optional.of((friends)));

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenAreFriendsTo() {
		final Set<String> juanFriends = newLinkedHashSet(pepe.getName());

		when(usersRepository.getFriends(pepe.getName())).thenReturn(Optional.of((emptySet())));
		when(usersRepository.getFriends(juan.getName())).thenReturn(Optional.of((juanFriends)));

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenThereIsNoRequestFrom() {
		when(usersRepository.getFriendShipRequests(pepe.getName())).thenReturn(Optional.of(emptySet()));

		friendShipService.accept(pepe, password, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenThereIsNoRequestTo() {
		final Set<String> pepeRequest = new HashSet<>();
		pepeRequest.add(juan.getName());

		when(usersRepository.getFriendShipRequests(juan.getName())).thenReturn(Optional.of(emptySet()));
		when(usersRepository.getFriendShipRequests(pepe.getName())).thenReturn(Optional.of(pepeRequest));

		friendShipService.accept(pepe, password, juan);
	}

	@Test
	public void shouldCallMethodAddAsFriend() {
		final LinkedHashSet<String> juanFriends = newLinkedHashSet("Margarita");
		final Set<String> juanRequests = newLinkedHashSet(pepe.getName());
		final LinkedHashSet<String> pepeFriends = new LinkedHashSet<>();
		final Set<String> pepeRequests = newLinkedHashSet(juan.getName());

		when(usersRepository.getFriendShipRequests(juan.getName())).thenReturn(Optional.of(juanRequests));
		when(usersRepository.getFriendShipRequests(pepe.getName())).thenReturn(Optional.of(pepeRequests));

		when(usersRepository.getFriends(juan.getName())).thenReturn(Optional.of((juanFriends)));
		when(usersRepository.getFriends(pepe.getName())).thenReturn(Optional.of((pepeFriends)));

		friendShipService.accept(pepe, password, juan);

		juanFriends.add(pepe.getName());
		verify(usersRepository, times(1)).addAsFriends(juan.getName(), juanFriends);
		pepeFriends.add(juan.getName());
		verify(usersRepository, times(1)).addAsFriends(pepe.getName(), pepeFriends);
		juanRequests.remove(pepe.getName());
		verify(usersRepository, times(1)).addRequest(juan.getName(), juanRequests);
		pepeRequests.remove(juan.getName());
		verify(usersRepository, times(1)).addRequest(pepe.getName(), pepeRequests);
	}
}
