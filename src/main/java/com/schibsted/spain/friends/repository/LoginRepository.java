package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.Username;
import org.springframework.stereotype.Component;

@Component
public class LoginRepository {

	public boolean userExists(String existingUser) {
		return false;
	}

	public void saveUser(Username user, Password password) {

	}
}
