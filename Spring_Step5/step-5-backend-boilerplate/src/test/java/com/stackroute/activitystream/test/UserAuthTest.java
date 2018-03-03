package com.stackroute.activitystream.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import javax.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.stackroute.activitystream.config.ApplicationContextConfig;
import com.stackroute.activitystream.config.PersistenceJPAConfig;
import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = { ApplicationContextConfig.class, PersistenceJPAConfig.class })
public class UserAuthTest {

	@Autowired
	private UserService userService;

	/*
	 * @Autowired private SessionFactory sessionFactory;
	 */

	@Before
	public void setup() {
		if (userService.get("john") != null) {
			userService.delete(userService.get("john"));
		}
		if (userService.get("will") != null) {
			userService.delete(userService.get("will"));
		}

		User testUser = new User();
		testUser.setName("John");
		testUser.setPassword("password");
		testUser.setUsername("john");
		userService.save(testUser);

		User testUser2 = new User();
		testUser2.setName("Will");
		testUser2.setPassword("password");
		testUser2.setUsername("will");
		userService.save(testUser2);

	}

	@After
	public void teardown() {

	}

	@Test
	public void testUserAuthentication() {

		assertNotNull("Authentication failed for legitimate user.", userService.validate("john", "password"));

	}

	@Test
	public void testUserAuthenticationFailure() {

		assertNotEquals(
				"Authentication logic is not correct. Please check. User with incorrect password is also being logged in",
				true, userService.validate("john", "password2"));

	}

}
