package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.util.Sets.newHashSet;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.junit.Assert.*;
import static org.mockito.internal.util.collections.Sets.newSet;

public class UsersInMemoryRepositoryTest {

	private final UsersInMemoryRepository usersRepository = new UsersInMemoryRepository();
	private final User user = new User("userName");
	private final Password password = new Password("12345678");
	private final Set<User> userFriendRequests = newSet(new User("requestOne"), new User("requestTwo"));
	private final LinkedHashSet<User> userFriends = newLinkedHashSet(new User("friendOne"), new User("friendTwo"));

	@Before
	public void setUp() {
		usersRepository.save(user, password);
		usersRepository.addRequest(user, userFriendRequests);
		usersRepository.addAsFriends(user, userFriends);
	}

	@Test
	public void shouldReturnFalseWhenUserDoesntExist() {
		assertFalse(usersRepository.userExists(new User("userUser")));
	}

	@Test
	public void shouldReturnTrueWhenUserIsSaved() {
		assertTrue(usersRepository.userExists(user));
	}

	@Test
	public void shouldReturnNullWhenThereIsNoUser() {
		assertNull(usersRepository.getPassword(new User("userUser")));
	}

	@Test
	public void shouldReturnPasswordTrueWhenUserIsSaved() {
		final Password actual = usersRepository.getPassword(user);

		assertEquals(password, actual);
	}

	@Test
	public void shouldReturnEmpty() {
		final Set expected = newHashSet();
		final Set<User> actual = usersRepository.getFriendShipRequests(new User("userUser"));

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnRequestSet() {
		final Set<User> expected = userFriendRequests;
		final Set<User> actual = usersRepository.getFriendShipRequests(user);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnEmptyFriends() {
		final LinkedHashSet<User> expected = newLinkedHashSet();
		final Set<User> actual = usersRepository.getFriends(new User("userUser"));

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnExpectedFriends() {
		final LinkedHashSet expected = userFriends;
		final Set<User> actual = usersRepository.getFriends(user);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnUpdatedFriends() {
		final LinkedHashSet<User> userFriends =
				newLinkedHashSet(
						new User("friendOne"),
						new User("friendTwo"),
						new User("friend3")
				);
		usersRepository.addAsFriends(user, userFriends);

		final Set<User> expected = userFriends;
		final Set<User> actual = usersRepository.getFriends(user);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnUpdatedFriendsRequests() {
		final Set<User> userFriendRequests =
				newSet(
						new User("userUser"),
						new User("userTwo"),
						new User("userThree")

				);
		usersRepository.addRequest(user, userFriendRequests);

		final Set<User> expected = userFriendRequests;
		final Set<User> actual = usersRepository.getFriendShipRequests(user);

		assertEquals(expected, actual);
	}
}