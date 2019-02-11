package com.schibsted.spain.friends.repository;

public interface UsersRepo {
	void save(String user, String password);

	boolean userExists(String existingUser);
}
