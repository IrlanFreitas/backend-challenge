package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UsersRepository usersRepository;

	@Autowired
	public UserService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public void saveUser(User user, Password password) {
		checkIfUserExists(user);

		usersRepository.save(user.getName(), password.getPassword());
	}

	boolean checkLogin(User user, Password password) {
		checkIfUserExists(user);
		return password.getPassword().equals(usersRepository.getPassword(user.getName()));
	}

	private void checkIfUserExists(User user) {
		if (usersRepository.exists(user.getName())) {
			throw new IllegalArgumentException();
		}
	}
}
