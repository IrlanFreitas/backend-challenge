package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.User;

import java.util.Optional;
import java.util.Set;

public interface RequestsRepository {

	void addRequest(User user, Set<User> list);

	Optional<Set<User>> getFriendShipRequests(User user);
}
