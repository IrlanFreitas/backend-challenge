package com.schibsted.spain.friends.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.junit.Assert.*;
import static org.mockito.internal.util.collections.Sets.newSet;

public class UsersInMemoryRepositoryTest {

	private final UsersInMemoryRepository usersRepository = new UsersInMemoryRepository();
	private final String password = "123456";
	private final String user = "user";
	private final Set<String> userFriendRequests = newSet("requestOne", "requestTwo");
	private final LinkedHashSet<String> userFriends = newLinkedHashSet("friendOne", "friendTwo");

	@Before
	public void setUp() {
		usersRepository.save(user, password);
		usersRepository.addRequest(user, userFriendRequests);
		usersRepository.addAsFriends(user, userFriends);
	}

	@Test
	public void shouldReturnFalseWhenUserDoesntExist() {
		assertFalse(usersRepository.userExists("notExisting"));
	}

	@Test
	public void shouldReturnTrueWhenUserIsSaved() {
		assertTrue(usersRepository.userExists(user));
	}

	@Test
	public void shouldReturnNullWhenThereIsNoUser() {
		assertNull(usersRepository.getPassword("notExisting"));
	}

	@Test
	public void shouldReturnPasswordTrueWhenUserIsSaved() {
		final String actual = usersRepository.getPassword(user);

		assertEquals(password, actual);
	}

	@Test
	public void shouldReturnEmpty() {
		final Optional<Set> expected = Optional.empty();
		final Optional<Set<String>> actual = usersRepository.getFriendShipRequests("notExisting");

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnRequestSet() {
		final Optional<Set> expected = Optional.of(userFriendRequests);
		final Optional<Set<String>> actual = usersRepository.getFriendShipRequests(user);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnEmptyFriends() {
		final Optional<LinkedHashSet> expected = Optional.empty();
		final Optional<Set<String>> actual = usersRepository.getFriends("notExisting");

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnExpectedFriends() {
		final Optional<LinkedHashSet> expected = Optional.of(userFriends);
		final Optional<Set<String>> actual = usersRepository.getFriends(user);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnUpdatedFriends() {
		final LinkedHashSet<String> userFriends = newLinkedHashSet("friendOne", "friendTwo", "friendThree");
		usersRepository.addAsFriends(user, userFriends);

		final Optional<Set> expected = Optional.of(userFriends);
		final Optional<Set<String>> actual = usersRepository.getFriends(user);

		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnUpdatedFriendsRequests() {
		final Set<String> userFriendRequests = newSet("requestOne", "requestTwo", "requestThree");
		usersRepository.addRequest(user, userFriendRequests);

		final Optional<Set<String>> expected = Optional.of(userFriendRequests);
		final Optional<Set<String>> actual = usersRepository.getFriendShipRequests(user);

		assertEquals(expected, actual);
	}
}
