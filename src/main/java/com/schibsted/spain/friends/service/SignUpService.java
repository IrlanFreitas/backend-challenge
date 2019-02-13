package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;

public class SignUpService {

	private final UsersRepository usersRepository;

	public SignUpService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public void saveUser(User user, Password password) {
		checkIfUserExists(user);
		usersRepository.save(user, password);
	}

	private void checkIfUserExists(User user) {
		if (usersRepository.userExists(user)) {
			throw new IllegalArgumentException("Username exists, try with another one.");
		}
	}
}
