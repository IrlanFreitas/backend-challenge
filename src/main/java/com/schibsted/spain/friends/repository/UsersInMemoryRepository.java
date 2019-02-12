package com.schibsted.spain.friends.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class UsersInMemoryRepository implements UsersRepository {
	private static Map<String, String> users = new HashMap<>();
	private static Map<String, Set<String>> requestList = new ConcurrentHashMap<>();
	private static Map<String, LinkedHashSet<String>> friendList = new ConcurrentHashMap<>();

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

	@Override
	public void addAsFriends(String user, LinkedHashSet<String> list) {
		friendList.put(user, list);
	}

	@Override
	public Optional<Set<String>> getFriends(String user) {
		return ofNullable(friendList.get(user));
	}
}
