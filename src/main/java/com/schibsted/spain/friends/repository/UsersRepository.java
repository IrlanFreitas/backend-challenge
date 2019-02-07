package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.RelationShip;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class UsersRepository {

	private static Map<String, String> users = new HashMap<>();
	private static Set<RelationShip> friendShipRequests = new HashSet<>();
	private static Set<RelationShip> friends = new HashSet<>();

	public boolean userExists(String existingUser) {
		return users.containsKey(existingUser);
	}

	public void save(String user, String password) {
		users.put(user, password);
	}

	public String getPassword(String username) {
		return users.get(username);
	}

	public boolean getFriendShipRequests(RelationShip relationShip) {
		return friendShipRequests.contains(relationShip);
	}

	public void addRequest(RelationShip relationShip) {
		friendShipRequests.add(relationShip);
	}

	public void deleteRequest(RelationShip relationShip) {
		friendShipRequests.remove(relationShip);
	}

	public void addAsFriends(RelationShip relationShip) {
		friends.add(relationShip);
	}
}
