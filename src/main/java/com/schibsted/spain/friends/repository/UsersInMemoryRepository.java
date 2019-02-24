package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsersInMemoryRepository implements FriendsRepository, UsersRepository, RequestsRepository {

	private final Map<User, Password> users = new HashMap<>();
	private final Map<User, Set<User>> requestsMap = new ConcurrentHashMap<>();
	private final Map<User, LinkedHashSet<User>> friendsMap = new ConcurrentHashMap<>();

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
	public Set<User> getFriendShipRequests(User user) {
		if (requestsMap.containsKey(user)) {
			return requestsMap.get(user);
		}
		return new HashSet<>();
	}

	@Override
	public void addAsFriends(User user, LinkedHashSet<User> list) {
		friendsMap.put(user, list);
	}

	@Override
	public LinkedHashSet<User> getFriends(User user) {
		if (friendsMap.containsKey(user)) {
			return friendsMap.get(user);
		}
		return new LinkedHashSet<>();
	}
}
