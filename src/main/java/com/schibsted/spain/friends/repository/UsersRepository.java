package com.schibsted.spain.friends.repository;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UsersRepository {

	private static Map<String, String> users = new HashMap<>();

	public boolean userExists(String existingUser) {
		return users.containsKey(existingUser);
	}

	public void save(String user, String password) {
		users.put(user, password);
	}

	public String getPassword(String username) {
		return users.get(username);
	}
}
