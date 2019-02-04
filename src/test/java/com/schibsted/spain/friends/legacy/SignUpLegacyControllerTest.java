package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.UserService;
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
@WebMvcTest(SignUpLegacyController.class)
public class SignUpLegacyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private final User userValid = new User("userValid");
	private final Password passwordValid = new Password("123456789ab");
	private static final String PATH = "/signup";
	private static final String PASS_HEADER = "X-Password";
	private static final String USERNAME = "username";

	@Test
	public void expectedBadRequestWhenPassIsNotOk() throws Exception {
		mockMvc.perform(post(PATH)
				.header(PASS_HEADER, "123456")
				.param(USERNAME, userValid.getName())
		).andExpect(status().is4xxClientError());
	}

	@Test
	public void expectedBadRequestWhenUserIsNotOk() throws Exception {
		mockMvc.perform(post(PATH)
				.header(PASS_HEADER, passwordValid.getPassword())
				.param(USERNAME, "qwe")
		).andExpect(status().is4xxClientError());
	}

	@Test
	public void expectedStatusOkWhenInputIsOk() throws Exception {
		mockMvc.perform(post(PATH)
				.header(PASS_HEADER, "123456789ab")
				.param(USERNAME, userValid.getName())
		).andExpect(status().is2xxSuccessful());

		verify(userService, times(1)).saveUser(userValid, passwordValid);
	}
}
