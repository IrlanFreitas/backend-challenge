package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class SignUpLegacyController {

	private final UserService userService;

	@Autowired
	public SignUpLegacyController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	ResponseEntity signUp(
			@RequestParam("username") String username,
			@RequestHeader("X-Password") String password
	) {
		userService.saveUser(new User(username), new Password(password));
		return new ResponseEntity(HttpStatus.OK);
	}
}
