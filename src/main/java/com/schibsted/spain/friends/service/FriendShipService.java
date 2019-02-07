package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.RelationShip;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendShipService {

	private UsersRepository usersRepository;

	@Autowired
	FriendShipService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public void request(User userFrom, Password password, User userTo) {
		checkInputs(userFrom, password, userTo);
		addFriendShipRequest(new RelationShip(userFrom, userTo));
	}

	public void accept(User from, Password password, User to) {
		checkInputs(from, password, to);
		acceptFriendShipRequest(new RelationShip(from, to));
	}

	public void decline(User from, Password password, User to) {
		checkInputs(from, password, to);
		declineFriendShipRequest(new RelationShip(from, to));
	}

	private void acceptFriendShipRequest(RelationShip relationShip) {
		checkAreFriends(relationShip);
		checkRequestedBefore(relationShip);
		usersRepository.deleteRequest(relationShip);
		usersRepository.addAsFriends(relationShip);
	}

	private void declineFriendShipRequest(RelationShip relationShip) {
		checkRequestedBefore(relationShip);
		usersRepository.deleteRequest(relationShip);
	}

	private void addFriendShipRequest(RelationShip relationShip) {
		checkAreFriends(relationShip);
		checkDontRequestedBefore(relationShip);
		usersRepository.addRequest(relationShip);
	}

	private void checkInputs(User userFrom, Password password, User userTo) {
		checkIfUsersExist(userFrom);
		checkIfUsersExist(userTo);
		checkLogin(userFrom, password);
	}

	private void checkDontRequestedBefore(RelationShip relationShip) {
		if (usersRepository.getFriendShipRequests(relationShip)) {
			throw new IllegalArgumentException("Users have a friendship request.");
		}
	}

	private void checkRequestedBefore(RelationShip relationShip) {
		if (!usersRepository.getFriendShipRequests(relationShip)) {
			throw new IllegalArgumentException("Users have no friendship request.");
		}
	}

	private void checkAreFriends(RelationShip relationShip) {
		if (usersRepository.getFriends(relationShip)) {
			throw new IllegalArgumentException("Users are friends.");
		}
	}

	private void checkLogin(User userFrom, Password password) {
		if (!password.getPassword().equals(usersRepository.getPassword(userFrom.getName()))) {
			throw new IllegalArgumentException("Password wrong for user " + userFrom);
		}
	}

	private void checkIfUsersExist(User user) {
		if (!usersRepository.userExists(user.getName())) {
			throw new IllegalArgumentException("User " + user + " doesnt exist.");
		}
	}
}
