package com.schibsted.spain.friends.repository;

import java.util.Optional;
import java.util.Set;

public interface UsersRepo {
	void save(String user, String password);

	boolean userExists(String existingUser);

	String getPassword(String username);

	void addRequest(String user, Set<String> list);

	Optional<Set<String>> getFriendShipRequests(String user);
}
