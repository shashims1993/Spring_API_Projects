package com.stackroute.activitystream.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.repository.UserRepository;
import com.stackroute.activitystream.service.UserService;
import com.stackroute.activitystream.service.UserServiceImpl;
/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
//@RequestMapping("/api/user")
@RestController
@CrossOrigin
public class UserController {
	/*
	 * Autowiring should be implemented for the UserService. Please note that we
	 * should not create any object using the new keyword
	 */
	@Autowired
	UserService userService;

	

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in user table
	 * in database. This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED) - If the user is successfully
	 * created 2. 409(CONFLICT) - If the username conflicts with any existing user
	 * 
	 * Note: ------ This method can be called without being logged in as well as
	 * when a new user will use the app, he will register himself first before
	 * login. This handler method should map to the URL "/api/user" using HTTP POST
	 * method
	 */

	@PostMapping(path = "/user")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		System.out.println("In Post mthod");
		if (userService.get(user.getUsername()) != null) {

			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}
		userService.save(user);

		return new ResponseEntity<User>(user, HttpStatus.CREATED);

	}

	
	@GetMapping(path = "/api/user")
	public ResponseEntity<?> getAllUserName(HttpSession session) {

		List<User> list = userService.list();
		if (list.size() > 0) {
			return new ResponseEntity<List<User>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("No User found in DB", HttpStatus.NOT_FOUND);
		}

	}


}