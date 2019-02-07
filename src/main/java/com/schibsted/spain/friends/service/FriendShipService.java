package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;

class FriendShipService {

	private UsersRepository usersRepository;

	FriendShipService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	void request(User userFrom, Password password, User userTo) {
		checkIfUsersExist(userFrom, userTo);
	}

	private void checkIfUsersExist(User userFrom, User userTo) {
		if (!usersRepository.userExists(userFrom.getName()) || !usersRepository.userExists(userTo.getName())) {
			throw new IllegalArgumentException();
		}
	}
}
