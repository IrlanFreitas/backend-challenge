package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.RelationShip;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FriendShipServiceTest {

	@Mock
	private UsersRepository usersRepository;

	private FriendShipService friendShipService;
	private User notExisting = new User("notExists");
	private User existingUser = new User("Existing");
	private Password password = new Password("passWord123");
	private User pepe = new User("Pepito");
	private User juan = new User("Juanito");

	@Before
	public void setUp() {
		friendShipService = new FriendShipService(usersRepository);
		when(usersRepository.userExists(notExisting.getName())).thenReturn(false);
		when(usersRepository.userExists(existingUser.getName())).thenReturn(true);
		when(usersRepository.getPassword(existingUser.getName())).thenReturn(password.getPassword());
		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());
		when(usersRepository.userExists(pepe.getName())).thenReturn(true);
		when(usersRepository.userExists(juan.getName())).thenReturn(true);
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
		RelationShip relationShip = new RelationShip(pepe, juan);

		when(usersRepository.getFriendShipRequests(relationShip)).thenReturn(true);

		friendShipService.request(pepe, password, juan);
	}

	@Test
	public void shouldCallAddFriendShipWhenInputIsOK() {
		RelationShip relationShip = new RelationShip(pepe, juan);

		when(usersRepository.getFriendShipRequests(relationShip)).thenReturn(false);

		friendShipService.request(pepe, password, juan);

		verify(usersRepository, times(1)).addRequest(relationShip);
	}

	@Test
	public void shouldWorksWithNoFriendsReturn() {
		when(usersRepository.getPassword(pepe.getName())).thenReturn(password.getPassword());

		friendShipService.request(pepe, password, juan);

		RelationShip relationShip = new RelationShip(pepe, juan);

		verify(usersRepository, times(1)).addRequest(relationShip);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfUserHasNoRequest() {
		RelationShip relationShip = new RelationShip(pepe, juan);

		when(usersRepository.getFriendShipRequests(relationShip)).thenReturn(false);

		friendShipService.accept(pepe, password, juan);
	}

	@Test
	public void shouldCallMethodAddAsFriend() {
		RelationShip relationShip = new RelationShip(pepe, juan);

		when(usersRepository.getFriendShipRequests(relationShip)).thenReturn(true);

		friendShipService.accept(pepe, password, juan);

		verify(usersRepository, times(1)).deleteRequest(relationShip);
		verify(usersRepository, times(1)).addAsFriends(relationShip);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenRequestAreFriends() {
		RelationShip relationShip = new RelationShip(pepe, juan);

		when(usersRepository.getFriends(relationShip)).thenReturn(true);

		friendShipService.request(pepe, password, juan);

		verify(usersRepository, times(1)).deleteRequest(relationShip);
		verify(usersRepository, times(1)).addAsFriends(relationShip);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenAcceptAreFriends() {
		RelationShip relationShip = new RelationShip(pepe, juan);

		when(usersRepository.getFriends(relationShip)).thenReturn(true);

		friendShipService.accept(pepe, password, juan);

		verify(usersRepository, times(1)).deleteRequest(relationShip);
		verify(usersRepository, times(1)).addAsFriends(relationShip);
	}
}
