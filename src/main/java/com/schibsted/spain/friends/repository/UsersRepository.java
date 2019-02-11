package com.schibsted.spain.friends.repository;

import java.util.*;

import static java.util.Optional.ofNullable;

public class UsersRepository {

	private static Map<String, String> users = new HashMap<>();
	private static Map<String, Set<String>> requestList = new HashMap<>();
	private static Map<String, LinkedHashSet<String>> friendList = new TreeMap<>();

	public boolean userExists(String existingUser) {
		return users.containsKey(existingUser);
	}

	public void save(String user, String password) {
		users.put(user, password);
	}

	public String getPassword(String username) {
		return users.get(username);
	}

	public Optional<Set<String>> getFriendShipRequests(String user) {
		return ofNullable(requestList.get(user));
	}

	public Optional<Set<String>> getFriends(String user) {
		return ofNullable(friendList.get(user));
	}

	public void addRequest(String user, Set<String> list) {
		requestList.put(user, list);
	}

	public void deleteRequest(String user, Set<String> list) {
		addRequest(user, list);
	}

	public void addAsFriends(String user, LinkedHashSet<String> list) {
		friendList.put(user, list);
	}
}
