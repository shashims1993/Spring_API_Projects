package com.stackroute.activitystream.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import javax.transaction.Transactional;
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
@ContextConfiguration(classes = { ApplicationContextConfig.class,PersistenceJPAConfig.class })
public class UserTest {

	@Autowired
	private UserService userService;

	@Autowired
	private User user;

	
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
		userService.save(testUser);

	}

	@After
	public void teardown() {
		
		if (userService.get("john") != null) {
			userService.delete(userService.get("john"));
		}
		if (userService.get("will") != null) {
			userService.delete(userService.get("will"));
		}
		if (userService.get("chris") != null) {
			userService.delete(userService.get("chris"));
		}
	}

	@Test
	public void testCreateUser() {

		user.setName("Chris");
		user.setPassword("password");
		user.setUsername("chris");
		userService.save(user);
		assertNotNull("Creating of user failed.", userService.get("chris"));
		userService.delete(user);
	}

	@Test
	public void testUpdateUser() {

		user=userService.get("john");
		user.setPassword("password2");
		userService.save(user);
		assertEquals("password2", userService.get("john").getPassword());
	}
	
	@Test
	public void testGetListOfUsers() {

		List<User> users = (List<User>) userService.list();
		assertNotNull("Retrieval of users failed.", users);
	}

	@Test
	public void testGetUser() {

		user = userService.get("john");
		assertNotNull("Retrieval of user failed.", user);
	}

}
