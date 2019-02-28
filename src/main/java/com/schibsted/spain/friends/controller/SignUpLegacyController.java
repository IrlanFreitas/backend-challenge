package com.schibsted.spain.friends.controller;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.SignUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.schibsted.spain.friends.configuration.Router.*;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping(SIGN_UP_REQUEST_MAPPING)
public class SignUpLegacyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignUpLegacyController.class);

	private final SignUpService signUpService;

	public SignUpLegacyController(SignUpService signUpService) {
		this.signUpService = signUpService;
	}

	@PostMapping
	ResponseEntity signUp(
			@RequestParam(USERNAME) String username,
			@RequestHeader(X_PASS) String password
	) {
		LOGGER.info("Received sing up from user {}", username);

		signUpService.saveUser(new User(username), new Password(password));

		return new ResponseEntity(OK);
	}
}
