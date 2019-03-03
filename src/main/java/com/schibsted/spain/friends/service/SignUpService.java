package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.FriendsRepository;
import com.schibsted.spain.friends.repository.RequestsRepository;
import com.schibsted.spain.friends.repository.UsersRepository;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class SignUpService {

	private final UsersRepository usersRepository;
	private final FriendsRepository friendsRepository;
	private final RequestsRepository requestsRepository;

	public SignUpService(UsersRepository usersRepository,
						 FriendsRepository friendsRepository,
						 RequestsRepository requestsRepository) {
		this.usersRepository = usersRepository;
		this.friendsRepository = friendsRepository;
		this.requestsRepository = requestsRepository;
	}

	public synchronized void saveUser(User user, Password password) {
		checkIfUserExists(user);
		usersRepository.save(user, password);
		friendsRepository.addFriends(user, new LinkedHashSet<>());
		requestsRepository.addRequests(user, new HashSet<>());
	}

	private void checkIfUserExists(User user) {
		if (usersRepository.userExists(user)) {
			throw new BadRequestException("Username exists, try with another one.");
		}
	}
}
