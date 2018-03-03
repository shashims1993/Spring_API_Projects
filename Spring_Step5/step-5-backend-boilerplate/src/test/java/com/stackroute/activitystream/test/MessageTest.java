package com.stackroute.activitystream.test;

import static org.junit.Assert.assertEquals;
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
import com.stackroute.activitystream.model.Circle;
import com.stackroute.activitystream.model.Message;
import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.model.UserCircle;
import com.stackroute.activitystream.service.UserService;
import com.stackroute.activitystream.serviceimpl.CircleServiceImpl;
import com.stackroute.activitystream.serviceimpl.MessageServiceImpl;
import com.stackroute.activitystream.serviceimpl.UserCircleServiceImpl;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = { ApplicationContextConfig.class })
public class MessageTest {

	@Autowired
	private MessageServiceImpl messageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	UserCircleServiceImpl userCircleService;

	@Autowired
	private CircleServiceImpl circleService;

	@Autowired
	private Message message;
	
	@Autowired
	private Circle circle;
	
	@Autowired
	private UserCircle userCircle;

	@Before
	public void setup() {
		if (userService.get("john") != null) {
			userService.delete(userService.get("john"));
		}
		
		if (userService.get("will") != null) {
			userService.delete(userService.get("will"));
		}
		
		if (circleService.get("Java") != null) {
			circleService.delete(circleService.get("Java"));
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

		Circle testCircle = new Circle();
		testCircle.setCircleName("Java");
		testCircle.setCreatedDate();
		testCircle.setCreatorId("john");
		circleService.save(testCircle);
		
		userCircleService.addUser("john", "Java");
		userCircleService.addUser("will", "Java");
	}

	@After
	public void teardown() {
		if (userService.get("john") != null) {
			userService.delete(userService.get("john"));
		}
		
		if (userService.get("will") != null) {
			userService.delete(userService.get("will"));
		}

		if (circleService.get("Java") != null) {
			circleService.delete(circleService.get("Java"));
		}
		
		if (userCircleService.get("john", "Java") != null) {
			userCircleService.removeUser("john","Java");
		}
		
		if (userCircleService.get("will", "Java") != null) {
			userCircleService.removeUser("will","Java");
		}
	}

	@Test
	public void testSendMessageToCircle() {
		message.setMessage("Sample Message");
		message.setStreamType("text");
		message.setSenderName("john");
		assertEquals("Sending message to Circle failed",true,messageService.sendMessageToCircle("Java", message));
		
	}
	
	@Test
	public void testSendMessageToCircleInvalidSenderFailure() {
		message.setMessage("Sample Message");
		message.setStreamType("text");
		message.setSenderName("chris");
		assertEquals("Sending message to Circle failed",false,messageService.sendMessageToCircle("Java", message));
		
	}
	
	@Test
	public void testSendMessageToCircleInvalidCircleFailure() {
		message.setMessage("Sample Message");
		message.setStreamType("text");
		message.setSenderName("john");
		assertEquals("Sending message to Circle failed",false,messageService.sendMessageToCircle("Angular", message));
		
	}
	
	@Test
	public void testSendMessageToUser() {
		message.setMessage("Sample Message");
		message.setStreamType("text");
		message.setSenderName("john");
		message.setTag("sampleTag");
		assertEquals("Sending message to Circle failed",true,messageService.sendMessageToUser("will", message));
		
	}
	
	@Test
	public void testSendMessageToInvalidSenderFailure() {
		message.setMessage("Sample Message");
		message.setStreamType("text");
		message.setSenderName("chris");
		assertEquals("Sending message to Circle failed",false,messageService.sendMessageToUser("will", message));
		
	}
	
	@Test
	public void testSendMessageToInvalidReceiverFailure() {
		message.setMessage("Sample Message");
		message.setStreamType("text");
		message.setSenderName("john");
		assertEquals("Sending message to Circle failed",false,messageService.sendMessageToUser("chris", message));
		
	}
	
	
	
}
