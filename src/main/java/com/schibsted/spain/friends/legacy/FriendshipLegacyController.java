package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.FriendShipService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  void acceptFriendship(
      @RequestParam("usernameFrom") String usernameFrom,
      @RequestParam("usernameTo") String usernameTo,
      @RequestHeader("X-Password") String password
  ) {
    throw new RuntimeException("not implemented yet!");
  }

  @PostMapping("/decline")
  void declineFriendship(
      @RequestParam("usernameFrom") String usernameFrom,
      @RequestParam("usernameTo") String usernameTo,
      @RequestHeader("X-Password") String password
  ) {
    throw new RuntimeException("not implemented yet!");
  }

  @GetMapping("/list")
  Object listFriends(
      @RequestParam("username") String username,
      @RequestHeader("X-Password") String password
  ) {
    throw new RuntimeException("not implemented yet!");
  }
}
