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
		if (loginRepository.userExists(username.getUsername())) {
			throw new IllegalArgumentException();
		}
		loginRepository.saveUser(username, password);
		return true;
	}
}
