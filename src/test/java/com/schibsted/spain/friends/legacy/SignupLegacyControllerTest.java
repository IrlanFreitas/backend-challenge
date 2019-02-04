package com.schibsted.spain.friends.legacy;

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

	@Test
	public void expectedBadRequestWhenPassHasLessThan8Char() throws Exception {
		String path = "/signup";
		String passHeader = "X-Password";
		String username = "username";

		mockMvc.perform(post(path)
				.header(passHeader, "123456")
				.param(username, userValid.getUsername())
		).andExpect(status().is4xxClientError());
	}
}
