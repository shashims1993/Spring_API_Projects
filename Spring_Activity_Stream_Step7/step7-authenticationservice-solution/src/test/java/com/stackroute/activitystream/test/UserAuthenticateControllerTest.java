package com.stackroute.activitystream.test;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.activitystream.config.JwtFilter;
import com.stackroute.activitystream.controller.UserAuthenticateController;
import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.service.UserService;
 
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserAuthenticateControllerTest {


	private MockMvc userAuthMockMvc;

	@Mock
	private User user;

	@Mock
	private UserService userService;

	

	@InjectMocks
	private UserAuthenticateController userAuthenticateController = new UserAuthenticateController();

	@InjectMocks
	private JwtFilter jwtFilter = new JwtFilter();

	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		userAuthMockMvc = MockMvcBuilders.standaloneSetup(userAuthenticateController).build();

	}

	
	
	@Test
	public void testLogin() throws Exception {
		
		String userName = "john";
		String password = "password";
		User user = new User(userName, userName, password);
		when(userService.get(userName)).thenReturn(null);
		when(userService.validate(userName, password)).thenReturn(true);

		userAuthMockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.message", is("user successfully logged in")))
				.andReturn();

	}

	@Test
	public void testLoginFailure() throws Exception {
		
		String userName = "john1";
		String password = "password1";
		User user = new User(userName, userName, password);
		when(userService.get(userName)).thenReturn(null);
		when(userService.validate(userName, password)).thenReturn(false);

		userAuthMockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				.andExpect(status().isUnauthorized()).andExpect(jsonPath("$.message", is("Invalid credentials.")))
				.andReturn();

	}
	

	/*
	 * converts a Java object into JSON representation
	 */

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
