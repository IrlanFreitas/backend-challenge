package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import com.schibsted.spain.friends.exceptions.NotFoundException;
import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.FriendsRepository;
import com.schibsted.spain.friends.repository.RequestsRepository;
import com.schibsted.spain.friends.repository.UsersRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FriendShipService {

	private final UsersRepository usersRepository;
	private final FriendsRepository friendsRepository;
	private final RequestsRepository requestsRepository;

	public FriendShipService(
			UsersRepository usersRepository,
			FriendsRepository friendsRepository,
			RequestsRepository requestsRepository
	) {
		this.usersRepository = usersRepository;
		this.friendsRepository = friendsRepository;
		this.requestsRepository = requestsRepository;
	}

	public void request(User from, Password password, User to) {
		checkInputs(from, password, to);

		requestFriendship(from, to);
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
		checkAreNotFriends(from, to);
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

	private void updateAccept(User user, User friend) {
		deleteRequest(user, friend);
		updateFriend(user, friend);
	}

	private synchronized void declineRequest(User from, User to) {
		checkRequestExists(from, to);

		deleteRequest(from, to);
		deleteRequest(to, from);
	}

	private void deleteRequest(User user, User request) {
		final Set<User> requests = requestsRepository.getFriendShipRequests(user);

		requests.remove(request);

		requestsRepository.addRequests(user, requests);
	}

	private void updateFriend(User from, User to) {
		final Set<User> friends = friendsRepository.getFriends(from);

		friends.add(to);

		friendsRepository.addFriends(from, (LinkedHashSet<User>) friends);
	}

	private void requestFriendship(User from, User to) {
		checkUserNotSameRequested(from, to);
		checkAreNotFriends(from, to);
		checkDontRequestedBefore(from, to);

		saveRequests(from, to);
	}

	private synchronized void saveRequests(User from, User to) {
		addRequest(from, to);
		addRequest(to, from);
	}

	private void addRequest(User user, User request) {
		final Set<User> list = requestsRepository.getFriendShipRequests(user);

		list.add(request);

		requestsRepository.addRequests(user, list);
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
		checkRequest(from, to);
		checkRequest(to, from);
	}

	private void checkAreNotFriends(User from, User to) {
		checkUserContainsInFriendList(from, to);
		checkUserContainsInFriendList(to, from);
	}

	private void checkUserContainsInFriendList(User user, User friend) {
		if (friendsRepository.getFriends(user).contains(friend)) {
			throw new BadRequestException("Users are friends.");
		}
	}

	private void checkRequest(User user, User request) {
		if (!requestsRepository.getFriendShipRequests(user).contains(request)) {
			throw new NotFoundException("There is no friend request.");
		}
	}

	private void checkLogin(User user, Password password) {
		if (!passwordCorrect(user, password)) {
			throw new BadRequestException("Wrong password.");
		}
	}

	private boolean passwordCorrect(User user, Password password) {
		return usersRepository
				.getPassword(user)
				.map(password::equals)
				.orElse(false);
	}

	private void checkIfUsersExist(User user) {
		if (!usersRepository.userExists(user)) {
			throw new NotFoundException("User doesn't exist.");
		}
	}

	private void checkDontRequest(User user, User request) {
		if (requestsRepository.getFriendShipRequests(user).contains(request)) {
			throw new BadRequestException("User has this request yet.");
		}
	}

	private void checkUserNotSameRequested(User user, User request) {
		if (user.equals(request)) {
			throw new BadRequestException("Can't request himself.");
		}
	}
}
