package com.schibsted.spain.friends.configuration;

import com.schibsted.spain.friends.controller.FriendshipLegacyController;
import com.schibsted.spain.friends.controller.SignUpLegacyController;
import com.schibsted.spain.friends.repository.FriendsRepository;
import com.schibsted.spain.friends.repository.RequestsRepository;
import com.schibsted.spain.friends.repository.UsersInMemoryRepository;
import com.schibsted.spain.friends.repository.UsersRepository;
import com.schibsted.spain.friends.service.FriendShipService;
import com.schibsted.spain.friends.service.SignUpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Conf {

	@Bean
	public UsersInMemoryRepository usersInMemoryRepository() {
		return new UsersInMemoryRepository();
	}

	@Bean
	public SignUpService signUpService(
			UsersRepository usersRepository,
			FriendsRepository friendsRepository,
			RequestsRepository requestsRepository
	) {
		return new SignUpService(usersRepository, friendsRepository, requestsRepository);
	}

	@Bean
	public FriendShipService friendShipService(
			UsersRepository usersRepository,
			FriendsRepository friendsRepository,
			RequestsRepository requestsRepository
	) {
		return new FriendShipService(usersRepository, friendsRepository, requestsRepository);
	}

	@Bean
	public SignUpLegacyController signUpLegacyController(
			SignUpService signUpService
	) {
		return new SignUpLegacyController(signUpService);
	}

	@Bean
	public FriendshipLegacyController friendshipLegacyController(
			FriendShipService friendShipService
	) {
		return new FriendshipLegacyController(friendShipService);
	}
}
