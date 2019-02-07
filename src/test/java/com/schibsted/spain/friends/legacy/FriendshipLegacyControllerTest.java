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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
	private static final String FRIENDSHIP_PATH = "/friendship/";
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
}
