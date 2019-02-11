package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.schibsted.spain.friends.legacy.Router.*;

@RestController
@RequestMapping(SIGN_UP_REQUEST_MAPPING)
public class SignUpLegacyController {

	private final SignUpService signUpService;

	@Autowired
	public SignUpLegacyController(SignUpService signUpService) {
		this.signUpService = signUpService;
	}

	@PostMapping
	ResponseEntity signUp(
			@RequestParam(USERNAME) String username,
			@RequestHeader(X_PASSWORD) String password
	) {
		signUpService.saveUser(new User(username), new Password(password));
		return new ResponseEntity(HttpStatus.OK);
	}
}
