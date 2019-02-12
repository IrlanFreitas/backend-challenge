package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.repository.UsersInMemoryRepository;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;

public class FriendShipService {

	private UsersInMemoryRepository usersRepository;

	public FriendShipService(UsersInMemoryRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public void request(User userFrom, Password password, User userTo) {
		checkInputs(userFrom, password, userTo);
		requestFriendship(userFrom.getName(), userTo.getName());
	}

	public void accept(User from, Password password, User to) {
		checkInputs(from, password, to);
		acceptFriendShipRequest(from.getName(), to.getName());
	}

	public void decline(User from, Password password, User to) {
		checkInputs(from, password, to);
		declineFriendShipRequest(from.getName(), to.getName());
	}

	private void acceptFriendShipRequest(String from, String to) {
		throwExceptionIfFriends(from, to);
		throwIfNotRequestExists(from, to);
		saveAccept(from, to);
	}

	public Set<String> list(User user, Password password) {
		checkIfUsersExist(user);
		checkLogin(user, password);
		if (usersRepository.getFriends(user.getName()).isPresent()) {
			return usersRepository.getFriends(user.getName()).get();
		}
		return emptySet();
	}

	private void saveAccept(String from, String to) {
		updateAccept(from, to);
		updateAccept(to, from);
	}

	private void updateAccept(String from, String to) {
		saveDecline(from, to);
		addAsFriends(from, to);
	}

	private void declineFriendShipRequest(String from, String to) {
		throwIfNotRequestExists(from, to);
		saveDecline(from, to);
		saveDecline(to, from);
	}

	private void saveDecline(String from, String to) {
		Set<String> list = new HashSet<>();
		usersRepository.getFriendShipRequests(from).ifPresent(list::addAll);
		list.remove(to);
		usersRepository.addRequest(from, list);
	}

	private void addAsFriends(String from, String to) {
		final LinkedHashSet<String> list = new LinkedHashSet<>();
		usersRepository.getFriends(from).ifPresent(list::addAll);
		list.add(to);
		usersRepository.addAsFriends(from, list);
	}

	private void requestFriendship(String from, String to) {
		throwExceptionIfFriends(from, to);
		checkDontRequestedBefore(from, to);
		saveFriendShipRequest(from, to);
	}

	private void saveFriendShipRequest(String from, String to) {
		saveFriendShipSet(from, to);
		saveFriendShipSet(to, from);
	}

	private void saveFriendShipSet(String from, String to) {
		final Set<String> list = new HashSet<>();
		usersRepository.getFriendShipRequests(from).ifPresent(list::addAll);

		list.add(to);

		usersRepository.addRequest(from, list);
	}

	private void checkInputs(User userFrom, Password password, User userTo) {
		checkIfUsersExist(userFrom);
		checkIfUsersExist(userTo);
		checkLogin(userFrom, password);
	}

	private void checkDontRequestedBefore(String from, String to) {
		checkDontRequest(from, to);
		checkDontRequest(to, from);
	}

	private void throwIfNotRequestExists(String from, String to) {
		throwIfNotRequest(from, to);
		throwIfNotRequest(to, from);
	}

	private void throwExceptionIfFriends(String from, String to) {
		ifFriendsThrowException(from, to);
		ifFriendsThrowException(to, from);
	}

	private void ifFriendsThrowException(String from, String to) {
		if (usersRepository.getFriends(from).isPresent() && usersRepository.getFriends(from).get().contains(to)) {
			throw new IllegalArgumentException("Users are friends.");
		}
	}

	private void throwIfNotRequest(String from, String to) {
		if (!usersRepository.getFriendShipRequests(from).isPresent() || !usersRepository.getFriendShipRequests(from).get().contains(to)) {
			throw new IllegalArgumentException("There is no friend request");
		}
	}

	private void checkLogin(User userFrom, Password password) {
		if (!password.getPassword().equals(usersRepository.getPassword(userFrom.getName()))) {
			throw new IllegalArgumentException("Wrong password");
		}
	}

	private void checkIfUsersExist(User user) {
		if (!usersRepository.userExists(user.getName())) {
			throw new IllegalArgumentException("User doesn't exist");
		}
	}

	private void checkDontRequest(String from, String to) {
		final Optional<Set<String>> friendShipRequests = usersRepository.getFriendShipRequests(from);
		if (friendShipRequests.isPresent() && friendShipRequests.get().contains(to)) {
			throw new IllegalArgumentException("User has this request yet");
		}
	}
}
