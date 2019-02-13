package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class UsersInMemoryRepository implements UsersRepository {

	private final static Map<User, Password> users = new HashMap<>();
	private final static Map<User, Set<User>> requestsMap = new ConcurrentHashMap<>();
	private final static Map<User, Set<User>> friendsMap = new ConcurrentHashMap<>();

	@Override
	public void save(User user, Password password) {
		users.put(user, password);
	}

	@Override
	public boolean userExists(User existingUser) {
		return users.containsKey(existingUser);
	}

	@Override
	public Password getPassword(User username) {
		return users.get(username);
	}

	@Override
	public void addRequest(User user, Set<User> list) {
		requestsMap.put(user, list);
	}

	@Override
	public Optional<Set<User>> getFriendShipRequests(User user) {
		return ofNullable(requestsMap.get(user));
	}

	@Override
	public void addAsFriends(User user, LinkedHashSet<User> list) {
		friendsMap.put(user, list);
	}

	@Override
	public Optional<Set<User>> getFriends(User user) {
		return ofNullable(friendsMap.get(user));
	}
}
