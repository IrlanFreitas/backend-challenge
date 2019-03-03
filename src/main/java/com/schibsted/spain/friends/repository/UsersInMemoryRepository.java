package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class UsersInMemoryRepository implements FriendsRepository, UsersRepository, RequestsRepository {

	private final Map<User, Password> users = new ConcurrentHashMap<>();
	private final Map<User, Set<User>> requestsMap = new ConcurrentHashMap<>();
	private final Map<User, Set<User>> friendsMap = new ConcurrentHashMap<>();

	@Override
	public void save(User user, Password password) {
		users.put(user, password);
	}

	@Override
	public boolean userExists(User existingUser) {
		return users.containsKey(existingUser);
	}

	@Override
	public Optional<Password> getPassword(User username) {
		return ofNullable(users.get(username));
	}

	@Override
	public void addRequests(User user, Set<User> list) {
		requestsMap.put(user, list);
	}

	@Override
	public Set<User> getFriendShipRequests(User user) {
		return requestsMap.containsKey(user) ? requestsMap.get(user) : new HashSet<>();
	}

	@Override
	public void addFriends(User user, LinkedHashSet<User> list) {
		friendsMap.put(user, list);
	}

	@Override
	public Set<User> getFriends(User user) {
		return friendsMap.containsKey(user) ? friendsMap.get(user) : new LinkedHashSet<>();
	}
}
