package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.Username;
import com.schibsted.spain.friends.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

	private final LoginRepository loginRepository;

	@Autowired
	public LoginService(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	boolean saveUser(Username username, Password password) {
		checkIfUserExists(username);

		loginRepository.saveUser(username.getUsername(), password.getPassword());

		return true;
	}

	public boolean checkLogin(Username username, Password password) {
		checkIfUserExists(username);
		return password.getPassword().equals(loginRepository.getPassword(username.getUsername()));
	}

	private void checkIfUserExists(Username username) {
		if (loginRepository.userExists(username.getUsername())) {
			throw new IllegalArgumentException();
		}
	}
}
