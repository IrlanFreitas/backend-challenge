package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.FriendShipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.schibsted.spain.friends.configuration.Router.*;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping(FRIENDSHIP_REQUEST_MAPPING)
public class FriendshipLegacyController {

	private final FriendShipService friendShipService;
	private final Logger LOGGER = LoggerFactory.getLogger(FriendshipLegacyController.class);

	public FriendshipLegacyController(FriendShipService friendShipService) {
		this.friendShipService = friendShipService;
	}

	@PostMapping(REQUEST)
	ResponseEntity requestFriendship(
			@RequestParam(USERNAME_FROM) String usernameFrom,
			@RequestParam(USERNAME_TO) String usernameTo,
			@RequestHeader(X_PASSWORD) String password
	) {
		LOGGER.info("Received friendShip request from {} to {}", usernameFrom, usernameTo);

		friendShipService.request(
				new User(usernameFrom),
				new Password(password),
				new User(usernameTo)
		);

		return new ResponseEntity(OK);
	}

	@PostMapping(ACCEPT)
	ResponseEntity acceptFriendship(
			@RequestParam(USERNAME_FROM) String usernameFrom,
			@RequestParam(USERNAME_TO) String usernameTo,
			@RequestHeader(X_PASSWORD) String password
	) {
		LOGGER.info("Received friendShip accept from {} to {}.", usernameFrom, usernameTo);

		friendShipService.accept(
				new User(usernameFrom),
				new Password(password),
				new User(usernameTo)
		);

		return new ResponseEntity(OK);
	}

	@PostMapping(DECLINE)
	ResponseEntity declineFriendship(
			@RequestParam(USERNAME_FROM) String usernameFrom,
			@RequestParam(USERNAME_TO) String usernameTo,
			@RequestHeader(X_PASSWORD) String password
	) {
		LOGGER.info("Received friendShip delete from {} to {}.", usernameFrom, usernameTo);

		friendShipService.decline(
				new User(usernameFrom),
				new Password(password),
				new User(usernameTo)
		);
		return new ResponseEntity(OK);
	}

	@GetMapping(LIST)
	Object listFriends(
			@RequestParam(USERNAME) String username,
			@RequestHeader(X_PASSWORD) String password
	) {
		LOGGER.info("Received friends list from {}.", username);

		return ResponseEntity.ok(friendShipService.list(new User(username), new Password(password)));
	}
}
