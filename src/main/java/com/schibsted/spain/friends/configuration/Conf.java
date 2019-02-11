package com.schibsted.spain.friends.configuration;

import com.schibsted.spain.friends.legacy.FriendshipLegacyController;
import com.schibsted.spain.friends.legacy.SignUpLegacyController;
import com.schibsted.spain.friends.repository.UsersRepository;
import com.schibsted.spain.friends.service.FriendShipService;
import com.schibsted.spain.friends.service.SignUpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Conf {

	@Bean
	public UsersRepository usersRepository() {
		return new UsersRepository();
	}

	@Bean
	public SignUpService signUpService() {
		return new SignUpService(usersRepository());
	}

	@Bean
	public FriendShipService friendShipService() {
		return new FriendShipService(usersRepository());
	}

	@Bean
	public SignUpLegacyController signUpLegacyController() {
		return new SignUpLegacyController(signUpService());
	}

	@Bean
	public FriendshipLegacyController friendshipLegacyController() {
		return new FriendshipLegacyController(friendShipService());
	}
}
