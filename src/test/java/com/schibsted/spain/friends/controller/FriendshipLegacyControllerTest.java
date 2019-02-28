package com.schibsted.spain.friends.controller;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.FriendShipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static com.schibsted.spain.friends.configuration.Router.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class FriendshipLegacyControllerTest {

	private final User userValid = new User("userValid");
	private final Password password = new Password("123456789ab");

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FriendShipService friendShipService;

	@Test
	public void expectedBadRequestWhenPassIsNotOk() throws Exception {
		mockMvc.perform(post(FRIENDSHIP_REQUEST_MAPPING + REQUEST)
				.header(X_PASS, "asd")
				.param(USERNAME_FROM, userValid.getName())
				.param(USERNAME_TO, userValid.getName())
		).andExpect(status().isBadRequest());
	}

	@Test
	public void expectedOkWhenParamsAreOk() throws Exception {
		mockMvc.perform(post(FRIENDSHIP_REQUEST_MAPPING + REQUEST)
				.header(X_PASS, "123456789ab")
				.param(USERNAME_FROM, userValid.getName())
				.param(USERNAME_TO, "Juanito")
		).andExpect(status().is2xxSuccessful());

		verify(friendShipService, times(1))
				.request(userValid, password, new User("Juanito"));
	}

	@Test
	public void expectedOkWhenAcceptAndParamsAreOk() throws Exception {
		mockMvc.perform(post(FRIENDSHIP_REQUEST_MAPPING + ACCEPT)
				.header(X_PASS, "123456789ab")
				.param(USERNAME_FROM, userValid.getName())
				.param(USERNAME_TO, "Juanito")
		).andExpect(status().is2xxSuccessful());

		verify(friendShipService, times(1))
				.accept(userValid, password, new User("Juanito"));
	}

	@Test
	public void expectedOkWhenDeclineAndParamsAreOk() throws Exception {
		mockMvc.perform(post(FRIENDSHIP_REQUEST_MAPPING + DECLINE)
				.header(X_PASS, "123456789ab")
				.param(USERNAME_FROM, userValid.getName())
				.param(USERNAME_TO, "Juanito")
		).andExpect(status().is2xxSuccessful());

		verify(friendShipService, times(1))
				.decline(userValid, password, new User("Juanito"));
	}

	@Test
	public void expectedOkWhenListAndParamsAreOk() {
		final var friendshipLegacyController = new FriendshipLegacyController(friendShipService);
		final var set = asList("Pepito", "Juanito");

		final var expected = ResponseEntity.ok(set);

		when(friendShipService.list(userValid, password)).thenReturn(set);

		final var actual = friendshipLegacyController.listFriends(userValid.getName(), "123456789ab");

		assertEquals(expected, actual);

		verify(friendShipService, times(1))
				.list(userValid, password);
	}

	@Test
	public void expectedEmptyWhenUserHasNoFriends() {
		final var friendshipLegacyController = new FriendshipLegacyController(friendShipService);
		final var emptySet = new ArrayList<String>();
		final var expected = ResponseEntity.ok(emptySet);

		when(friendShipService.list(userValid, password)).thenReturn(emptySet);

		final var actual = friendshipLegacyController.listFriends(userValid.getName(), "123456789ab");

		assertEquals(expected, actual);

		verify(friendShipService, times(1))
				.list(userValid, password);
	}

	@Test
	public void expectedOkWhenGetFriendsAndParamsAreOk() throws Exception {
		final var expected = new ArrayList<String>();

		when(friendShipService.list(userValid, password)).thenReturn(expected);

		mockMvc.perform(get(FRIENDSHIP_REQUEST_MAPPING + LIST)
				.header(X_PASS, "123456789ab")
				.param(USERNAME, userValid.getName())
		).andExpect(status().is2xxSuccessful());

		verify(friendShipService, times(1))
				.list(userValid, password);
	}
}
