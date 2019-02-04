package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.Username;
import com.schibsted.spain.friends.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class SignupLegacyController {

	private final LoginService loginService;

	@Autowired
	public SignupLegacyController(LoginService loginService) {
		this.loginService = loginService;
	}

	@PostMapping
	ResponseEntity signUp(
			@RequestParam("username") String username,
			@RequestHeader("X-Password") String password
	) {
		loginService.saveUser(new Username(username), new Password(password));
		return new ResponseEntity(HttpStatus.OK);
	}
}
