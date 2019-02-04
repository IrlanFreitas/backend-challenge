package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.Username;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SignupLegacyController.class)
public class SignupLegacyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final Username userValid = new Username("userValid");
	private final Password passwordValid = new Password("123456789ab");

	@Test
	public void expectedBadRequestWhenPassIsNotOk() throws Exception {
		String path = "/signup";
		String passHeader = "X-Password";
		String username = "username";

		mockMvc.perform(post(path)
				.header(passHeader, "123456")
				.param(username, userValid.getUsername())
		).andExpect(status().is4xxClientError());
	}

	@Test
	public void expectedBadRequestWhenUserIsNotOk() throws Exception {
		String path = "/signup";
		String passHeader = "X-Password";
		String username = "username";

		mockMvc.perform(post(path)
				.header(passHeader, passwordValid.getPassword())
				.param(username, "qwe")
		).andExpect(status().is4xxClientError());
	}
}
