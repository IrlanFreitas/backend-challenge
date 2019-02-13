package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public interface UsersRepository {

	void save(User user, Password password);

	boolean userExists(User existingUser);

	Password getPassword(User username);

	void addRequest(User user, Set<User> list);

	Optional<Set<User>> getFriendShipRequests(User user);

	void addAsFriends(User user, LinkedHashSet<User> list);

	Optional<Set<User>> getFriends(User user);
}
