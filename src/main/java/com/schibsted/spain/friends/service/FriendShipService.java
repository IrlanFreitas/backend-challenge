package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import com.schibsted.spain.friends.exceptions.NotFoundException;
import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.FriendsRepository;
import com.schibsted.spain.friends.repository.PasswordsRepository;
import com.schibsted.spain.friends.repository.RequestsRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FriendShipService {

	private final PasswordsRepository passwordsRepository;
	private final FriendsRepository friendsRepository;
	private final RequestsRepository requestsRepository;

	public FriendShipService(
			PasswordsRepository passwordsRepository,
			FriendsRepository friendsRepository,
			RequestsRepository requestsRepository
	) {
		this.passwordsRepository = passwordsRepository;
		this.friendsRepository = friendsRepository;
		this.requestsRepository = requestsRepository;
	}

	public void request(User userFrom, Password password, User userTo) {
		checkInputs(userFrom, password, userTo);

		requestFriendship(userFrom, userTo);
	}

	public void accept(User from, Password password, User to) {
		checkInputs(from, password, to);

		acceptFriendShipRequest(from, to);
	}

	public void decline(User from, Password password, User to) {
		checkInputs(from, password, to);

		declineRequest(from, to);
	}

	private void acceptFriendShipRequest(User from, User to) {
		checkArentFriends(from, to);
		checkRequestExists(from, to);

		saveAsFriends(from, to);
	}

	public List<String> list(User user, Password password) {
		checkIfUsersExist(user);
		checkLogin(user, password);

		return friendsRepository.getFriends(user)
				.stream()
				.map(User::getName)
				.collect(Collectors.toList());
	}

	private synchronized void saveAsFriends(User from, User to) {
		updateAccept(from, to);
		updateAccept(to, from);
	}

	private void updateAccept(User from, User to) {
		deleteRequest(from, to);
		updateFriend(from, to);
	}

	private synchronized void declineRequest(User from, User to) {
		checkRequestExists(from, to);

		deleteRequest(from, to);
		deleteRequest(to, from);
	}

	private void deleteRequest(User from, User to) {
		final Set<User> requests = requestsRepository.getFriendShipRequests(from);

		requests.remove(to);

		requestsRepository.addRequest(from, requests);
	}

	private void updateFriend(User from, User to) {
		final LinkedHashSet<User> friends = friendsRepository.getFriends(from);

		friends.add(to);

		friendsRepository.addAsFriends(from, friends);
	}

	private void requestFriendship(User from, User to) {
		checkUserNotSameRequested(from, to);
		checkArentFriends(from, to);
		checkDontRequestedBefore(from, to);

		saveRequests(from, to);
	}

	private synchronized void saveRequests(User from, User to) {
		addRequest(from, to);
		addRequest(to, from);
	}

	private void addRequest(User from, User to) {
		final Set<User> list = requestsRepository.getFriendShipRequests(from);

		list.add(to);

		requestsRepository.addRequest(from, list);
	}

	private void checkInputs(User userFrom, Password password, User userTo) {
		checkIfUsersExist(userFrom);
		checkIfUsersExist(userTo);
		checkLogin(userFrom, password);
	}

	private void checkDontRequestedBefore(User from, User to) {
		checkDontRequest(from, to);
		checkDontRequest(to, from);
	}

	private void checkRequestExists(User from, User to) {
		throwIfNotRequest(from, to);
		throwIfNotRequest(to, from);
	}

	private void checkArentFriends(User from, User to) {
		checkUserContainsInFriendList(from, to);
		checkUserContainsInFriendList(to, from);
	}

	private void checkUserContainsInFriendList(User from, User to) {
		if (friendsRepository.getFriends(from).contains(to)) {
			throw new BadRequestException("Users are friends.");
		}
	}

	private void throwIfNotRequest(User from, User to) {
		if (!requestsRepository.getFriendShipRequests(from).contains(to)) {
			throw new NotFoundException("There is no friend request.");
		}
	}

	private void checkLogin(User userFrom, Password password) {
		if (!password.equals(passwordsRepository.getPassword(userFrom))) {
			throw new BadRequestException("Wrong password.");
		}
	}

	private void checkIfUsersExist(User user) {
		if (!passwordsRepository.userExists(user)) {
			throw new NotFoundException("User doesn't exist.");
		}
	}

	private void checkDontRequest(User from, User to) {
		final Set<User> friendShipRequests = requestsRepository.getFriendShipRequests(from);
		if (friendShipRequests.contains(to)) {
			throw new BadRequestException("User has this request yet.");
		}
	}

	private void checkUserNotSameRequested(User from, User to) {
		if(from.equals(to)){
			throw new BadRequestException("Can't request himself.");
		}
	}
}
