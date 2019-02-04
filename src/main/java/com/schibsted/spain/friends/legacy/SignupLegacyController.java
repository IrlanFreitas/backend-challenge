package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.Username;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class SignupLegacyController {

	@PostMapping
	ResponseEntity signUp(
			@RequestParam("username") String username,
			@RequestHeader("X-Password") String password
	) {
		Password password1 = new Password(password);
		Username username1 = new Username(username);

		return new ResponseEntity(HttpStatus.OK);
	}
}
