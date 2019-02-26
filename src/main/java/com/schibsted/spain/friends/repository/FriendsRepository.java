package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.User;

import java.util.LinkedHashSet;

public interface FriendsRepository {

	void addAsFriends(User user, LinkedHashSet<User> list);

	LinkedHashSet<User> getFriends(User user);
}
