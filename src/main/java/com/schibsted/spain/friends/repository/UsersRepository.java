package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;

import java.util.Optional;

public interface UsersRepository {

	void save(User user, Password password);

	boolean userExists(User existingUser);

	Optional<Password> getPassword(User username);
}
