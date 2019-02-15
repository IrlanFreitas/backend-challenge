package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.SignUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.schibsted.spain.friends.configuration.Router.*;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping(SIGN_UP_REQUEST_MAPPING)
public class SignUpLegacyController {

	private final SignUpService signUpService;
	private final Logger LOGGER = LoggerFactory.getLogger(SignUpLegacyController.class);

	public SignUpLegacyController(SignUpService signUpService) {
		this.signUpService = signUpService;
	}

	@PostMapping
	ResponseEntity signUp(
			@RequestParam(USERNAME) String username,
			@RequestHeader(X_PASSWORD) String password
	) {
		LOGGER.info("Received sing up from user {}", username);

		signUpService.saveUser(new User(username), new Password(password));

		return new ResponseEntity(OK);
	}
}
