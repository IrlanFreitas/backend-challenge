package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.PasswordsRepository;

public class SignUpService {

	private final PasswordsRepository passwordsRepository;

	public SignUpService(PasswordsRepository passwordsRepository) {
		this.passwordsRepository = passwordsRepository;
	}

	public void saveUser(User user, Password password) {
		checkIfUserExists(user);
		passwordsRepository.save(user, password);
	}

	private void checkIfUserExists(User user) {
		if (passwordsRepository.userExists(user)) {
			throw new BadRequestException("Username exists, try with another one.");
		}
	}
}
