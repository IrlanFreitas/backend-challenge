package com.schibsted.spain.friends.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class UsersInMemoryRepository implements UsersRepository {

	private final static Map<String, String> users = new HashMap<>();
	private final static Map<String, Set<String>> requestsMap = new ConcurrentHashMap<>();
	private final static Map<String, LinkedHashSet<String>> friendsMap = new ConcurrentHashMap<>();

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
		requestsMap.put(user, list);
	}

	@Override
	public Optional<Set<String>> getFriendShipRequests(String user) {
		return ofNullable(requestsMap.get(user));
	}

	@Override
	public void addAsFriends(String user, LinkedHashSet<String> list) {
		friendsMap.put(user, list);
	}

	@Override
	public Optional<Set<String>> getFriends(String user) {
		return ofNullable(friendsMap.get(user));
	}
}
