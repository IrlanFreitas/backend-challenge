package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class FriendShipService {

	private UsersRepository usersRepository;

	public FriendShipService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
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
		declineFriendShipRequest(from, to);
	}

	private void acceptFriendShipRequest(User from, User to) {
		throwExceptionIfFriends(from, to);
		throwIfNotRequestExists(from, to);
		saveAccept(from, to);
	}

	public List<String> list(User user, Password password) {
		checkIfUsersExist(user);
		checkLogin(user, password);
		if (usersRepository.getFriends(user).isPresent()) {
			return usersRepository.getFriends(user).get().stream().map(User::getName).collect(Collectors.toList());
		}
		return emptyList();
	}

	private void saveAccept(User from, User to) {
		updateAccept(from, to);
		updateAccept(to, from);
	}

	private void updateAccept(User from, User to) {
		saveDecline(from, to);
		addAsFriends(from, to);
	}

	private void declineFriendShipRequest(User from, User to) {
		throwIfNotRequestExists(from, to);
		saveDecline(from, to);
		saveDecline(to, from);
	}

	private void saveDecline(User from, User to) {
		Set<User> list = new HashSet<>();
		usersRepository.getFriendShipRequests(from).ifPresent(list::addAll);
		list.remove(to);
		usersRepository.addRequest(from, list);
	}

	private void addAsFriends(User from, User to) {
		final LinkedHashSet<User> list = new LinkedHashSet<>();
		usersRepository.getFriends(from).ifPresent(list::addAll);
		list.add(to);
		usersRepository.addAsFriends(from, list);
	}

	private void requestFriendship(User from, User to) {
		throwExceptionIfFriends(from, to);
		checkDontRequestedBefore(from, to);
		saveFriendShipRequest(from, to);
	}

	private void saveFriendShipRequest(User from, User to) {
		saveFriendShipSet(from, to);
		saveFriendShipSet(to, from);
	}

	private void saveFriendShipSet(User from, User to) {
		final Set<User> list = new HashSet<>();
		usersRepository.getFriendShipRequests(from).ifPresent(list::addAll);

		list.add(to);

		usersRepository.addRequest(from, list);
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

	private void throwIfNotRequestExists(User from, User to) {
		throwIfNotRequest(from, to);
		throwIfNotRequest(to, from);
	}

	private void throwExceptionIfFriends(User from, User to) {
		ifFriendsThrowException(from, to);
		ifFriendsThrowException(to, from);
	}

	private void ifFriendsThrowException(User from, User to) {
		if (usersRepository.getFriends(from).isPresent() && usersRepository.getFriends(from).get().contains(to)) {
			throw new IllegalArgumentException("Users are friends.");
		}
	}

	private void throwIfNotRequest(User from, User to) {
		if (!usersRepository.getFriendShipRequests(from).isPresent() || !usersRepository.getFriendShipRequests(from).get().contains(to)) {
			throw new IllegalArgumentException("There is no friend request");
		}
	}

	private void checkLogin(User userFrom, Password password) {
		if (!password.equals(usersRepository.getPassword(userFrom))) {
			throw new IllegalArgumentException("Wrong password");
		}
	}

	private void checkIfUsersExist(User user) {
		if (!usersRepository.userExists(user)) {
			throw new IllegalArgumentException("User doesn't exist");
		}
	}

	private void checkDontRequest(User from, User to) {
		final Optional<Set<User>> friendShipRequests = usersRepository.getFriendShipRequests(from);
		if (friendShipRequests.isPresent() && friendShipRequests.get().contains(to)) {
			throw new IllegalArgumentException("User has this request yet");
		}
	}
}
