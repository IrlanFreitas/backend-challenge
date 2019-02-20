package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.FriendsRepository;
import com.schibsted.spain.friends.repository.PasswordsRepository;
import com.schibsted.spain.friends.repository.RequestsRepository;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class SignUpService {

	private final PasswordsRepository passwordsRepository;
	private final FriendsRepository friendsRepository;
	private final RequestsRepository requestsRepository;

	public SignUpService(PasswordsRepository passwordsRepository,
						 FriendsRepository friendsRepository,
						 RequestsRepository requestsRepository) {
		this.passwordsRepository = passwordsRepository;
		this.friendsRepository = friendsRepository;
		this.requestsRepository = requestsRepository;
	}

	public void saveUser(User user, Password password) {
		checkIfUserExists(user);
		passwordsRepository.save(user, password);
		friendsRepository.addAsFriends(user, new LinkedHashSet<>());
		requestsRepository.addRequest(user, new HashSet<>());
	}

	private void checkIfUserExists(User user) {
		if (passwordsRepository.userExists(user)) {
			throw new BadRequestException("Username exists, try with another one.");
		}
	}
}
