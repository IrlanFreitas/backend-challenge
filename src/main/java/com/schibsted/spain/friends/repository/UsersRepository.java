package com.schibsted.spain.friends.repository;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Component
public class UsersRepository {

	private static Map<String, String> users = new HashMap<>();
	private static Map<String, Set<String>> friendShipRequests = new HashMap<>();

	public boolean userExists(String existingUser) {
		return users.containsKey(existingUser);
	}

	public void save(String user, String password) {
		users.put(user, password);
	}

	public String getPassword(String username) {
		return users.get(username);
	}

	public Optional<Set<String>> getFriendShipRequests(String username) {
		return ofNullable(friendShipRequests.get(username));
	}

	public void addRequest(String name, Set<String> friends) {
		friendShipRequests.put(name, friends);
	}
}
