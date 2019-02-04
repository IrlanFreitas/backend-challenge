package com.schibsted.spain.friends.repository;

import org.springframework.stereotype.Component;

@Component
public class LoginRepository {

	public boolean userExists(String existingUser) {
		return false;
	}

	public void saveUser(String user, String password) {

	}

	public String getPassword(String username) {
		return null;
	}
}
