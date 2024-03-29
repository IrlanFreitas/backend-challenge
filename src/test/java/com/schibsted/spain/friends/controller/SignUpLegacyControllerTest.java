package com.schibsted.spain.friends.controller;

import com.schibsted.spain.friends.model.Password;
import com.schibsted.spain.friends.model.User;
import com.schibsted.spain.friends.service.SignUpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.schibsted.spain.friends.configuration.Router.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class SignUpLegacyControllerTest {

	private final User userValid = new User("userValid");

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SignUpService signUpService;

	@Test
	public void expectedBadRequestWhenPassIsNotOk() throws Exception {
		mockMvc.perform(post(SIGN_UP_REQUEST_MAPPING)
				.header(X_PASS, "123456")
				.param(USERNAME, userValid.getName())
		).andExpect(status().isBadRequest());
	}

	@Test
	public void expectedBadRequestWhenUserIsNotOk() throws Exception {
		mockMvc.perform(post(SIGN_UP_REQUEST_MAPPING)
				.header(X_PASS, "12345678")
				.param(USERNAME, "qwe")
		).andExpect(status().isBadRequest());
	}

	@Test
	public void expectedStatusOkWhenInputIsOk() throws Exception {
		final String value = "123456789ab";

		mockMvc.perform(post(SIGN_UP_REQUEST_MAPPING)
				.header(X_PASS, value)
				.param(USERNAME, userValid.getName())
		).andExpect(status().is2xxSuccessful());

		verify(signUpService, times(1)).saveUser(userValid, new Password(value));
	}
}
