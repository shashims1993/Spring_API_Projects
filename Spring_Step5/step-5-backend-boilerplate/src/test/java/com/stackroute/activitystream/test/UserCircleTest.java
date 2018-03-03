package com.stackroute.activitystream.test;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.stackroute.activitystream.config.ApplicationContextConfig;
import com.stackroute.activitystream.config.PersistenceJPAConfig;
import com.stackroute.activitystream.model.Circle;
import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.model.UserCircle;
import com.stackroute.activitystream.repository.CircleRepository;
import com.stackroute.activitystream.repository.UserCircleRepository;
import com.stackroute.activitystream.repository.UserRepository;
import com.stackroute.activitystream.serviceimpl.UserCircleServiceImpl;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = { ApplicationContextConfig.class,PersistenceJPAConfig.class })
public class UserCircleTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserCircleRepository userCircleRepository;

	@Autowired
	private CircleRepository circleRepository;
	
	@Autowired
	private UserCircleServiceImpl userCircleService;

	@Autowired
	private Circle circle;
	
	@Autowired
	private UserCircle userCircle;

	@Before
	public void setup() {
		if (userRepository.findOne("john") != null) {
			userRepository.delete(userRepository.findOne("john"));
		}

		if (circleRepository.findOne("Java") != null) {
			circleRepository.delete(circleRepository.findOne("Java"));
		}
		User testUser = new User();
		testUser.setName("John");
		testUser.setPassword("password");
		testUser.setUsername("john");
		userRepository.save(testUser);

		Circle testCircle = new Circle();
		testCircle.setCircleName("Java");
		testCircle.setCreatedDate();
		testCircle.setCreatorId("John");
		circleRepository.save(testCircle);

	}

	@After
	public void teardown() {
		if (userRepository.findOne("john") != null) {
			userRepository.delete(userRepository.findOne("john"));
		}

		if (circleRepository.findOne("Java") != null) {
			circleRepository.delete(circleRepository.findOne("Java"));
		}
	}

	@Test
	public void testAddUserToCircle() {
		
		assertEquals("Adding user to circle failed",true,userCircleService.addUser("john","Java"));
		
	}
	
	@Test
	public void testAddUserToCircleInvalidUserFailure() {

		assertEquals("Adding user to circle failed",false,userCircleService.addUser("chris", "Java"));
		
	}

	@Test
	public void testAddUserToCircleInvalidCircleFailure() {

		assertEquals("Adding user to circle failed",false,userCircleService.addUser("john", "Spring"));
		
	}
	
	@Test
	public void testRemoveUserFromCircle() {
		userCircleService.addUser("john", "Java");
		assertEquals("Removing user from circle failed",true,userCircleService.removeUser("john", "Java"));
		
	}
	
	@Test
	public void testRemoveUserFromCircleInvalidUserFailure() {

		assertEquals("Removing user from circle failed",false,userCircleService.removeUser("chris", "Java"));
		
	}

	@Test
	public void testRemoveUserFromCircleInvalidCircleFailure() {

		assertEquals("Removing user from circle failed",false,userCircleService.removeUser("john", "Spring"));
		
	}
	
	@Test
	public void testUserSubscriptionToCircle() {
		userCircleService.addUser("john", "Java");
		List<String> userSubscription=new ArrayList<String>();
		userSubscription.add("Java");
		assertEquals("Removing user from circle failed",userSubscription,userCircleService.getMyCircles("john"));
		
	}

}
