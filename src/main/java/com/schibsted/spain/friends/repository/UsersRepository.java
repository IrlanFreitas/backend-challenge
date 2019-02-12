package com.schibsted.spain.friends.repository;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public interface UsersRepository {

	void save(String user, String password);

	boolean userExists(String existingUser);

	String getPassword(String username);

	void addRequest(String user, Set<String> list);

	Optional<Set<String>> getFriendShipRequests(String user);

	void addAsFriends(String user, LinkedHashSet<String> list);

	Optional<Set<String>> getFriends(String user);
}
