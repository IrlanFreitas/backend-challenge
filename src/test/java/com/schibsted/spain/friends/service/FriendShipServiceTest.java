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
public class FriendShipServiceTest {

	@Mock
	private UsersRepository usersRepository;

	private FriendShipService friendShipService;
	private User notExisting = new User("notExists");
	private User existingUser = new User("Existing");
	private Password password = new Password("passWord123");


	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository);
		when(usersRepository.userExists(notExisting.getName())).thenReturn(false);
		when(usersRepository.userExists(existingUser.getName())).thenReturn(true);
		when(usersRepository.getPassword(existingUser.getName())).thenReturn(password.getPassword());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenFromUserDoesntExists() {
		friendShipService.request(notExisting, password, existingUser);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenToUserDoesntExists() {
		friendShipService.request(existingUser, password, notExisting);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenPasswordIsNotCorrect() {
		Password wrong = new Password("wrongPass");
		friendShipService.request(existingUser, wrong, existingUser);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenUsersAreInRequest() {
		User pepe = new User("Pepito");
		User juan = new User("Juanito");

		Set<String> pepesFriends = new HashSet<>();
		pepesFriends.add("Juanito");

		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());
		when(usersRepository.userExists(pepe.getName())).thenReturn(true);
		when(usersRepository.userExists(juan.getName())).thenReturn(true);
		when(usersRepository.getFriendShipRequests(pepe.getName())).thenReturn(Optional.of(pepesFriends));

		friendShipService.request(pepe, password, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenUsersAreInRequestInverse() {
		User pepe = new User("Pepito");
		User juan = new User("Juanito");

		Set<String> pepesFriends = new HashSet<>();
		pepesFriends.add("Raquel");

		Set<String> juanFriends = new HashSet<>();
		juanFriends.add("Margarita");
		juanFriends.add("Pepito");

		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());
		when(usersRepository.userExists(pepe.getName())).thenReturn(true);
		when(usersRepository.userExists(juan.getName())).thenReturn(true);
		when(usersRepository.getFriendShipRequests(pepe.getName())).thenReturn(Optional.of(pepesFriends));
		when(usersRepository.getFriendShipRequests(juan.getName())).thenReturn(Optional.of(juanFriends));

		friendShipService.request(pepe, password, juan);
	}

	@Test
	public void shouldCallAddFriendShipWhenInputIsOK() {
		User pepe = new User("Pepito");
		User juan = new User("Juanito");

		Set<String> pepesFriends = new HashSet<>();
		pepesFriends.add("Raquel");

		Set<String> juanFriends = new HashSet<>();
		juanFriends.add("Margarita");

		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());
		when(usersRepository.userExists(pepe.getName())).thenReturn(true);
		when(usersRepository.userExists(juan.getName())).thenReturn(true);
		when(usersRepository.getFriendShipRequests(pepe.getName())).thenReturn(Optional.of(pepesFriends));
		when(usersRepository.getFriendShipRequests(juan.getName())).thenReturn(Optional.of(juanFriends));

		friendShipService.request(pepe, password, juan);

		pepesFriends.add(juan.getName());
		juanFriends.add(pepe.getName());

		verify(usersRepository, times(1)).addRequest(juan.getName(), juanFriends);
		verify(usersRepository, times(1)).addRequest(pepe.getName(), pepesFriends);
	}

	@Test
	public void shouldWorksWithNoFriendsReturn() {
		User pepe = new User("Pepito");
		User juan = new User("Juanito");

		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());
		when(usersRepository.userExists(pepe.getName())).thenReturn(true);
		when(usersRepository.userExists(juan.getName())).thenReturn(true);
		when(usersRepository.getFriendShipRequests(pepe.getName())).thenReturn(Optional.empty());
		when(usersRepository.getFriendShipRequests(juan.getName())).thenReturn(Optional.empty());

		friendShipService.request(pepe, password, juan);

		Set<String> pepesFriends = new HashSet<>();
		Set<String> juanFriends = new HashSet<>();
		pepesFriends.add(juan.getName());
		juanFriends.add(pepe.getName());

		verify(usersRepository, times(1)).addRequest(juan.getName(), juanFriends);
		verify(usersRepository, times(1)).addRequest(pepe.getName(), pepesFriends);
	}
}
