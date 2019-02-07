package com.schibsted.spain.friends.repository;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class UsersRepository {

	private static Map<String, String> users = new HashMap<>();
	private static Map<String, Set<String>> friendShips = new HashMap<>();

	public boolean userExists(String existingUser) {
		return users.containsKey(existingUser);
	}

	public void save(String user, String password) {
		users.put(user, password);
	}

	public String getPassword(String username) {
		return users.get(username);
	}

	public Set<String> getFriendList(String username) {
		return friendShips.get(username);
	}
}
