package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class FriendShipService {

	private UsersRepository usersRepository;

	@Autowired
	FriendShipService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public void request(User userFrom, Password password, User userTo) {
		checkInputs(userFrom, password, userTo);
		addFriendShip(userFrom, userTo);
		addFriendShip(userTo, userFrom);
	}

	private void addFriendShip(User userFrom, User userTo) {
		Set<String> friends = new HashSet<>();

		usersRepository.getFriendShipList(userFrom.getName()).ifPresent(friends::addAll);
		friends.add(userTo.getName());

		usersRepository.addFriendShip(userFrom.getName(), friends);
	}

	private void checkInputs(User userFrom, Password password, User userTo) {
		checkIfUsersExist(userFrom, userTo);
		checkLogin(userFrom, password);
		checkFriendShip(userFrom, userTo);
		checkFriendShip(userTo, userFrom);
	}

	private void checkFriendShip(User userFrom, User userTo) {
		final Optional<Set<String>> friendList = usersRepository.getFriendShipList(userFrom.getName());
		if (friendList.isPresent() && friendList.get().contains(userTo.getName())) {
			throw new IllegalArgumentException();
		}
	}

	private void checkLogin(User userFrom, Password password) {
		if (!password.getPassword().equals(usersRepository.getPassword(userFrom.getName()))) {
			throw new IllegalArgumentException();
		}
	}

	private void checkIfUsersExist(User userFrom, User userTo) {
		if (!usersRepository.userExists(userFrom.getName()) || !usersRepository.userExists(userTo.getName())) {
			throw new IllegalArgumentException();
		}
	}
}
