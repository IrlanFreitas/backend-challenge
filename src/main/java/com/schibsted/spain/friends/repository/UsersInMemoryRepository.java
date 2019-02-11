package com.schibsted.spain.friends.repository;

import java.util.HashMap;
import java.util.Map;

public class UsersInMemoryRepository implements UsersRepo {
	private static Map<String, String> users = new HashMap<>();

	@Override
	public void save(String user, String password) {
		users.put(user, password);
	}

	@Override
	public boolean userExists(String existingUser) {
		return users.containsKey(existingUser);
	}
}
