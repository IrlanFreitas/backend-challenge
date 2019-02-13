package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersInMemoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeclineFriendShipServiceTest {

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
		friendShipService.decline(notExisting, password, pepe);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenToUserDoesntExists() {
		friendShipService.decline(pepe, password, notExisting);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenPasswordIsNotCorrect() {
		final Password wrong = new Password("wrongPass");
		friendShipService.decline(pepe, wrong, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfUserHasNoRequest() {
		friendShipService.decline(pepe, password, juan);
	}

	@Test
	public void shouldCallMethodAddAsFriend() {
		final Set<User> juanRequests = newLinkedHashSet(pepe);
		final Set<User> pepeRequests = newLinkedHashSet(juan);

		when(usersRepository.getFriendShipRequests(juan)).thenReturn(Optional.of(juanRequests));
		when(usersRepository.getFriendShipRequests(pepe)).thenReturn(Optional.of(pepeRequests));

		friendShipService.decline(pepe, password, juan);

		juanRequests.remove(pepe);
		verify(usersRepository, times(1)).addRequest(juan, juanRequests);
		pepeRequests.remove(juan);
		verify(usersRepository, times(1)).addRequest(pepe, pepeRequests);
	}
}
