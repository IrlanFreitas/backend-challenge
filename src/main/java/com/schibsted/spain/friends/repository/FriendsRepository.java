package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.User;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public interface FriendsRepository {

	void addAsFriends(User user, LinkedHashSet<User> list);

	Optional<Set<User>> getFriends(User user);
}
