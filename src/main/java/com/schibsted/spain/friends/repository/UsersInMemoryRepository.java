package com.schibsted.spain.friends.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class UsersInMemoryRepository implements UsersRepo {
	private static Map<String, String> users = new HashMap<>();
	private static Map<String, Set<String>> requestList = new ConcurrentHashMap<>();

	@Override
	public void save(String user, String password) {
		users.put(user, password);
	}

	@Override
	public boolean userExists(String existingUser) {
		return users.containsKey(existingUser);
	}

	@Override
	public String getPassword(String username) {
		return users.get(username);
	}

	@Override
	public void addRequest(String user, Set<String> list) {
		requestList.put(user, list);
	}

	@Override
	public Optional<Set<String>> getFriendShipRequests(String user) {
		return ofNullable(requestList.get(user));
	}

}
