package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.User;

import java.util.LinkedHashSet;
import java.util.Set;

public interface FriendsRepository {

	void addFriends(User user, LinkedHashSet<User> list);

	Set<User> getFriends(User user);
}
