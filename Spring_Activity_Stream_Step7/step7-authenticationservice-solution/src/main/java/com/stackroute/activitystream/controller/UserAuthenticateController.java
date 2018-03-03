package com.stackroute.activitystream.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * This class will be used to create RESTful microservice. Hence annotate
 * the class with @RestController annotation. A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 * 
 * Also annotate this class with @EnableWebMvc annotation to enable Spring Web MVC.
 */
@RestController
@CrossOrigin
public class UserAuthenticateController {

	Map<String, String> map = new HashMap<>();

	/*
	 * Autowiring should be implemented for the UserService. Please note that we
	 * should not create any object using the new keyword
	 */
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String serverStarted() {
		return "Authentication server started";
	}

	/*
	 * Define a handler method which will authenticate a user by reading the
	 * Serialized user object from request body containing the username and
	 * password. The username and password should be validated before proceeding
	 * ahead with JWT token generation. The user credentials will be validated
	 * against the database entries. The error should be return if validation is not
	 * successful. If credentials are validated successfully, then JWT token will be
	 * generated. The token should be returned back to the caller along with the API
	 * response. This handler method should return any one of the status messages
	 * basis on different situations: 1. 200(OK) - If login is successful 2.
	 * 401(UNAUTHORIZED) - If login is not successful
	 * 
	 * This handler method should map to the URL "/login1" using HTTP POST method
	 */

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody User user) throws ServletException {

		String jwtToken = "";

		try {

			jwtToken = getToken(user.getUsername(), user.getPassword());
			map.clear();
			map.put("message", "user successfully logged in");
			map.put("token", jwtToken);
		} catch (Exception e) {
			String exceptionMessage = e.getMessage();
			map.clear();
			map.put("token", null);
			map.put("message", exceptionMessage);
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	public String getToken(String username, String password) throws Exception {

		String jwtToken = "";

		if (username == null || password == null) {
			throw new ServletException("Please fill in username and password");
		}

		boolean flag = userService.validate(username, password);

		if (!flag) {
			throw new ServletException("Invalid credentials.");
		}

		jwtToken = Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretkey").compact();

		return jwtToken;

	}

}
