package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.FriendShipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FriendshipLegacyController.class)
public class FriendshipLegacyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FriendShipService friendShipService;

	private final User userValid = new User("userValid");
	private final Password password = new Password("123456789ab");
	private static final String FRIENDSHIP_PATH = "/friendship/request";
	private static final String ACCEPT_PATH = "/friendship/accept";
	private static final String DECLINE_PATH = "/friendship/decline";
	private static final String PASS_HEADER = "X-Password";
	private static final String USERNAME_FROM = "usernameFrom";
	private static final String USERNAME_TO = "usernameTo";

	@Test
	public void expectedBadRequestWhenPassIsNotOk() throws Exception {
		mockMvc.perform(post(FRIENDSHIP_PATH)
				.header(PASS_HEADER, "asd")
				.param(USERNAME_FROM, userValid.getName())
				.param(USERNAME_TO, userValid.getName())
		).andExpect(status().is4xxClientError());
	}

	@Test
	public void expectedOkWhenParamsAreOk() throws Exception {
		mockMvc.perform(post(FRIENDSHIP_PATH)
				.header(PASS_HEADER, "123456789ab")
				.param(USERNAME_FROM, userValid.getName())
				.param(USERNAME_TO, "Juanito")
		).andExpect(status().is2xxSuccessful());

		verify(friendShipService, times(1))
				.request(userValid, password, new User("Juanito"));
	}

	@Test
	public void expectedOkWhenAcceptAndParamsAreOk() throws Exception {
		mockMvc.perform(post(ACCEPT_PATH)
				.header(PASS_HEADER, "123456789ab")
				.param(USERNAME_FROM, userValid.getName())
				.param(USERNAME_TO, "Juanito")
		).andExpect(status().is2xxSuccessful());

		verify(friendShipService, times(1))
				.accept(userValid, password, new User("Juanito"));
	}

	@Test
	public void expectedOkWhenDeclineAndParamsAreOk() throws Exception {
		mockMvc.perform(post(DECLINE_PATH)
				.header(PASS_HEADER, "123456789ab")
				.param(USERNAME_FROM, userValid.getName())
				.param(USERNAME_TO, "Juanito")
		).andExpect(status().is2xxSuccessful());

		verify(friendShipService, times(1))
				.decline(userValid, password, new User("Juanito"));
	}

	@Test
	public void expectedOkWhenListAndParamsAreOk() {
		FriendshipLegacyController friendshipLegacyController = new FriendshipLegacyController(friendShipService);

		final List<String> expected = asList("Pepito", "Juanito");

		when(friendShipService.list(userValid, password)).thenReturn(expected);

		final Object actual = friendshipLegacyController.listFriends(userValid.getName(), "123456789ab");

		assertEquals(expected, actual);

		verify(friendShipService, times(1))
				.list(userValid, password);
	}

	@Test
	public void expectedEmptyWhenUserHasNoFriends() {
		FriendshipLegacyController friendshipLegacyController = new FriendshipLegacyController(friendShipService);

		final List<String> expected = emptyList();

		when(friendShipService.list(userValid, password)).thenReturn(expected);

		final Object actual = friendshipLegacyController.listFriends(userValid.getName(), "123456789ab");

		assertEquals(expected, actual);

		verify(friendShipService, times(1))
				.list(userValid, password);
	}
}
