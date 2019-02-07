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

import static org.mockito.Mockito.when;

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
	public void shouldThrowExpectedWhenUsersAreFriends() {
		User pepe = new User("Pepito");
		User juan = new User("Juanito");

		Set<String> pepesFriends = new HashSet<>();
		pepesFriends.add("Juanito");

		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());
		when(usersRepository.userExists(pepe.getName())).thenReturn(true);
		when(usersRepository.userExists(juan.getName())).thenReturn(true);
		when(usersRepository.getFriendShipList(pepe.getName())).thenReturn(Optional.of(pepesFriends));

		friendShipService.request(pepe, password, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExpectedWhenUsersAreFriendsInverse() {
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
		when(usersRepository.getFriendShipList(pepe.getName())).thenReturn(Optional.of(pepesFriends));
		when(usersRepository.getFriendShipList(juan.getName())).thenReturn(Optional.of(juanFriends));

		friendShipService.request(pepe, password, juan);
	}
}
