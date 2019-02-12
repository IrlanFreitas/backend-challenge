package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersInMemoryRepository;

public class SignUpService {

	private final UsersInMemoryRepository usersRepository;

	public SignUpService(UsersInMemoryRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public void saveUser(User user, Password password) {
		checkIfUserExists(user);
		usersRepository.save(user.getName(), password.getPassword());
	}

	private void checkIfUserExists(User user) {
		if (usersRepository.userExists(user.getName())) {
			throw new IllegalArgumentException("Username exists, try with another one.");
		}
	}
}
