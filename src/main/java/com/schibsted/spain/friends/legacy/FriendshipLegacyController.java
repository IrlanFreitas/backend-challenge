package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.FriendShipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.schibsted.spain.friends.legacy.Router.*;

@RestController
@RequestMapping(FRIENDSHIP_REQUEST_MAPPING)
public class FriendshipLegacyController {

	private FriendShipService friendShipService;

	public FriendshipLegacyController(FriendShipService friendShipService) {
		this.friendShipService = friendShipService;
	}

	@PostMapping(REQUEST)
	ResponseEntity requestFriendship(
			@RequestParam(USERNAME_FROM) String usernameFrom,
			@RequestParam(USERNAME_TO) String usernameTo,
			@RequestHeader(X_PASSWORD) String password
	) {
		friendShipService.request(
				new User(usernameFrom),
				new Password(password),
				new User(usernameTo)
		);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping(ACCEPT)
	ResponseEntity acceptFriendship(
			@RequestParam(USERNAME_FROM) String usernameFrom,
			@RequestParam(USERNAME_TO) String usernameTo,
			@RequestHeader(X_PASSWORD) String password
	) {
		friendShipService.accept(
				new User(usernameFrom),
				new Password(password),
				new User(usernameTo)
		);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping(DECLINE)
	ResponseEntity declineFriendship(
			@RequestParam(USERNAME_FROM) String usernameFrom,
			@RequestParam(USERNAME_TO) String usernameTo,
			@RequestHeader(X_PASSWORD) String password
	) {
		friendShipService.decline(
				new User(usernameFrom),
				new Password(password),
				new User(usernameTo)
		);
		return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping(LIST)
	Object listFriends(
			@RequestParam(USERNAME) String username,
			@RequestHeader(X_PASSWORD) String password
	) {
		return friendShipService.list(new User(username), new Password(password));
	}
}
