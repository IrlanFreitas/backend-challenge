package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.FriendShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friendship")
public class FriendshipLegacyController {

	private FriendShipService friendShipService;

	@Autowired
	public FriendshipLegacyController(FriendShipService friendShipService) {
		this.friendShipService = friendShipService;
	}

	@PostMapping("/request")
	ResponseEntity requestFriendship(
			@RequestParam("usernameFrom") String usernameFrom,
			@RequestParam("usernameTo") String usernameTo,
			@RequestHeader("X-Password") String password
	) {
		friendShipService.request(
				new User(usernameFrom),
				new Password(password),
				new User(usernameTo)
		);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/accept")
	ResponseEntity acceptFriendship(
			@RequestParam("usernameFrom") String usernameFrom,
			@RequestParam("usernameTo") String usernameTo,
			@RequestHeader("X-Password") String password
	) {
		friendShipService.accept(
				new User(usernameFrom),
				new Password(password),
				new User(usernameTo)
		);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/decline")
	ResponseEntity declineFriendship(
			@RequestParam("usernameFrom") String usernameFrom,
			@RequestParam("usernameTo") String usernameTo,
			@RequestHeader("X-Password") String password
	) {
		friendShipService.decline(
				new User(usernameFrom),
				new Password(password),
				new User(usernameTo)
		);
		return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping("/list")
	Object listFriends(
			@RequestParam("username") String username,
			@RequestHeader("X-Password") String password
	) {
		return friendShipService.list(new User(username), new Password(password));
	}
}
