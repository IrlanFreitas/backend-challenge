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
public class AcceptFriendShipServiceTest {

	@Mock
	private UsersRepository usersRepository;

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
		final RelationShip relationShip = new RelationShip(pepe, juan);

		when(usersRepository.getFriendShipRequests(relationShip)).thenReturn(false);

		friendShipService.accept(pepe, password, juan);
	}

	@Test
	public void shouldCallMethodAddAsFriend() {
		final RelationShip relationShip = new RelationShip(pepe, juan);

		when(usersRepository.getFriendShipRequests(relationShip)).thenReturn(true);

		friendShipService.accept(pepe, password, juan);

		verify(usersRepository, times(1)).deleteRequest(relationShip);
		verify(usersRepository, times(1)).addAsFriends(relationShip);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenAcceptAreFriends() {
		final RelationShip relationShip = new RelationShip(pepe, juan);

		when(usersRepository.getFriends(relationShip)).thenReturn(true);

		friendShipService.accept(pepe, password, juan);
	}
}
