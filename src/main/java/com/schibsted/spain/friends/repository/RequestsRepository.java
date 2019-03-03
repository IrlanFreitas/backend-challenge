package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.User;

import java.util.Set;

public interface RequestsRepository {

	void addRequests(User user, Set<User> list);

	Set<User> getFriendShipRequests(User user);
}
