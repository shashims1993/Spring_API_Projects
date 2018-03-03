package com.stackroute.activitystream.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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
import com.stackroute.activitystream.service.CircleService;
import com.stackroute.activitystream.service.UserService;



@RunWith(SpringRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = { ApplicationContextConfig.class, PersistenceJPAConfig.class })
public class CircleTest {

	@Autowired
	private UserService userService;

	@Autowired
	private CircleService circleService;

	@Autowired
	private Circle circle;

	@Before
	public void setup() {
		if (userService.get("john") != null) {
			userService.delete(userService.get("john"));
		}

		if (circleService.get("Java") != null) {
			circleService.delete(circleService.get("Java"));
		}
		User testUser = new User();
		testUser.setName("John");
		testUser.setPassword("password");
		testUser.setUsername("john");
		userService.save(testUser);

		Circle testCircle = new Circle();
		testCircle.setCircleName("Java");
		testCircle.setCreatedDate();
		testCircle.setCreatorId("John");
		circleService.save(testCircle);

	}

	@After
	public void teardown() {
		if (userService.get("john") != null) {
			userService.delete(userService.get("john"));
		}

		if (circleService.get("Java") != null) {
			circleService.delete(circleService.get("Java"));
		}
	}

	@Test
	public void testCreateCircle() {

		circle.setCircleName("Angular");
		circle.setCreatedDate();
		circle.setCreatorId("john");
		circleService.save(circle);
		assertNotNull("Creating of circle failed.", circleService.get("Angular"));
		circleService.delete(circle);
	}

	@Test
	public void testCreateCircleInvalidCreatorFailure() {

		circle.setCircleName("Angular");
		circle.setCreatedDate();
		circle.setCreatorId("chris");
		assertNotEquals("Creation of circle with invalid creatorID successful", true, circleService.save(circle));
		circleService.delete(circle);
	}

	@Test
	public void testCreateCircleDuplicateFailure() {

		circle.setCircleName("Java");
		circle.setCreatedDate();
		circle.setCreatorId("john");
		circleService.save(circle);
		assertNotEquals("Creation of circle with the same name as of an existing circle successful", true,
				circleService.save(circle));
		circleService.delete(circle);
	}

	@Test
	public void testGetAllCircles() {

		assertNotNull("Creation of circle with the same name as of an existing circle successful",
				circleService.getAllCircles());
		
	}
	
	@Test
	public void testGetAllCirclesWithSearchString() {

		assertNotNull("Retrieval of all circles unsuccessful",
				circleService.getAllCircles("Java"));
	}
	
	@Test
	public void testGetAllCirclesWithSearchStringFailure() {

		assertNotNull("Retrieval of all circles containing a Search String unsuccessful",
				circleService.getAllCircles("Spring"));
	}

}