package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

	private final UsersRepository usersRepository;

	@Autowired
	public SignUpService(UsersRepository usersRepository) {
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
