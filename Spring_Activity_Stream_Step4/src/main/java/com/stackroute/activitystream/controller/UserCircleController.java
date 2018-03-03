package com.stackroute.activitystream.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.activitystream.dao.CircleDAO;
import com.stackroute.activitystream.dao.UserCircleDAO;
import com.stackroute.activitystream.dao.UserDAO;
import com.stackroute.activitystream.model.Circle;
import com.stackroute.activitystream.model.Message;
import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.model.UserCircle;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RequestMapping("api/usercircle")
@RestController
public class UserCircleController {

	/*
	 * Autowiring should be implemented for the
	 * UserDAO,UserCircleDAO,CircleDAO,UserCircle. Please note that we should not
	 * create any object using the new keyword
	 */
	@Autowired
	UserDAO userDAO;
	@Autowired
	CircleDAO circleDAO;
	@Autowired
	UserCircleDAO userCircleDAO;
	@Autowired
	UserCircle userCircle;

	/*
	 * Define a handler method which will add a user to a circle.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the user is added to the circle 2.
	 * 500(INTERNAL SERVER ERROR) - If there are any errors 3. 401(UNAUTHORIZED) -
	 * If the user is not logged in 4. 404(NOT FOUND) - if the username or the
	 * circle does not exist 5. 409(CONFLICT) - if the user is already added to the
	 * circle
	 * 
	 * This handler method should map to the URL
	 * "/api/usercircle/addToCircle/{username}/{circleName}" using HTTP PUT method"
	 * where "username" should be replaced by a valid username without {} and
	 * "circleName" should be replaced by a valid circle name without {}
	 */
	@PutMapping(path = "/addToCircle/{username}/{circleName}")
	public ResponseEntity<?> addUserToCircle(@PathVariable("username") String username,
			@PathVariable("circleName") String circleName, HttpSession session) {

		String logginUserName = (String) session.getAttribute("LoginUserName");
		if (logginUserName != null) {

			/*
			 * User user=userDAO.get(username) ; Circle circle=circleDAO.get(circleName);
			 */

			if (userDAO.get(username) == null || circleDAO.get(circleName) == null) {
				return new ResponseEntity<String>("Username or circle name does not exist", HttpStatus.NOT_FOUND);
			} else {

				userCircle = userCircleDAO.get(username, circleName);
				if (userCircle != null) {
					return new ResponseEntity<String>("User is already added to the Circle", HttpStatus.CONFLICT);
				} else {

					userCircleDAO.addUser(username, circleName);
					return new ResponseEntity<String>("Added to the Circle", HttpStatus.OK);

				}

			}
		}

		else {
			return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

	}

	/*
	 * @GetMapping public ResponseEntity<?> getByUserName(HttpSession session) {
	 * 
	 * if (session.getAttribute("LoginUserName") != null) {
	 * 
	 * userCircle=userCircleDAO.get("john", "spring");
	 * System.out.println(userCircle.getCircleName()); return new
	 * ResponseEntity<String>(userCircle.getCircleName(), HttpStatus.OK); } else {
	 * return new ResponseEntity<String>(userCircle.getCircleName(),
	 * HttpStatus.UNAUTHORIZED); } }
	 */

	/*
	 * Define a handler method which will remove a user from a circle.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the user is remove from the circle 2.
	 * 500(INTERNAL SERVER ERROR) - If there are any errors 3. 401(UNAUTHORIZED) -
	 * If the user is not logged in
	 * 
	 * This handler method should map to the URL
	 * "/api/usercircle/removeFromCircle/{username}/{circleName}" using HTTP PUT
	 * method" where "username" should be replaced by a valid username without {}
	 * and "circleName" should be replaced by a valid circle name without {}
	 */
	@PutMapping(path = "/removeFromCircle/{username}/{circleName}")
	public ResponseEntity<?> removeUserFromCircle(@PathVariable("username") String username,
			@PathVariable("circleName") String circleName, HttpSession session) {
		String logginUserName = (String) session.getAttribute("LoginUserName");
		if (logginUserName != null) {

			if (userCircleDAO.removeUser(username, circleName)) {
				return new ResponseEntity<String>("Removed from the Circle", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("User circle is not exist so not able to delete",
						HttpStatus.INTERNAL_SERVER_ERROR);

			}

		}

		else {
			return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

	}

	/*
	 * Define a handler method which will get us the subscribed circles by a user.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the user is added to the circle 2.
	 * 401(UNAUTHORIZED) - If the user is not logged in
	 * 
	 * This handler method should map to the URL // *
	 * "/api/usercircle/searchByUser/{username}" using HTTP GET method where
	 * "username" should be replaced by a valid username without {}
	 */
	@GetMapping(path = "/searchByUser/{username}")
	public ResponseEntity<?> getSubscribeCircleByUser(@PathVariable("username") String username, HttpSession session) {
		String logginUserName = (String) session.getAttribute("LoginUserName");
		if (logginUserName != null) {

			// if (userDAO.get(username) != null) {
			List<String> list = userCircleDAO.getMyCircles(username);

			if (list.size() > 0) {
				return new ResponseEntity<List<String>>(list, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No circle subscriptions found for the user", HttpStatus.NOT_FOUND);
			}

			// } else {
			// return new ResponseEntity<String>("Username does not exist",
			// HttpStatus.NOT_FOUND);
			// }
		}

		else {
			return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

	}

}
