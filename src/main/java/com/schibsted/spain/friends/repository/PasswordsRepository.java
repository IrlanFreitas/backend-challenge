package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;

public interface PasswordsRepository {

	void save(User user, Password password);

	boolean userExists(User existingUser);

	Password getPassword(User username);
}
