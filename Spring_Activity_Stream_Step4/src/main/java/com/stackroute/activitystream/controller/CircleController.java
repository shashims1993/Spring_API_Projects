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
@RequestMapping("api/circle")
@RestController
public class CircleController {

	/*
	 * From the problem statement, we can understand that the application requires
	 * us to implement three functionalities regarding circles. They are as
	 * following:
	 * 
	 * 1. Create a circle 2. Get all circles 3. Get all circles which is matching a
	 * keyword
	 * 
	 * we must also ensure that only a user who is logged in should be able to
	 * perform the functionalities mentioned above.
	 * 
	 */

	/*
	 * Autowiring should be implemented for the CircleDAO and UserCircle. Please
	 * note that we should not create any object using the new keyword
	 */
	@Autowired
	CircleDAO circleDAO;
	@Autowired
	Circle circle;

	/*
	 * Define a handler method which will create a circle by reading the Serialized
	 * circle object from request body and save the circle in circle table in
	 * database. Please note that the circleName has to be unique and the loggedIn
	 * userID should be taken as the creatorId for the circle. This handler method
	 * should return any one of the status messages basis on different situations:
	 * 1. 201(CREATED - In case of successful creation of the circle 2.
	 * 209(CONFLICT) - In case of duplicate circle ID 3. 401(UNAUTHORIZED) - If the
	 * user is not logged in
	 * 
	 * This handler method should map to the URL "/api/circle" using HTTP POST
	 * method".
	 */

	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody Circle circle, HttpSession session) {
		String logginUserName=(String) session.getAttribute("LoginUserName");
		if (logginUserName!= null) {
			Circle circleObj = circleDAO.get(circle.getCircleName());
			if (circleObj != null) {
				return new ResponseEntity<User>(HttpStatus.CONFLICT);

			}
			circle.setCreatorId(logginUserName);
			boolean flag=circleDAO.save(circle);
			if(!flag)
			{
				System.out.println("Error while saving the circle");
			}
			return new ResponseEntity<Circle>(circle, HttpStatus.CREATED);

		} else {
			return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

	}
	
	

	/*
	 * Define a handler method which will retrieve all the available circles. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - In case of success 2. 401(UNAUTHORIZED) -
	 * If the user is not logged in
	 * 
	 * This handler method should map to the URL "/api/circle" using HTTP GET
	 * method".
	 */
	@GetMapping
	public ResponseEntity<?> getAllCircleName(HttpSession session) {
		if (session.getAttribute("LoginUserName") != null) {
			List<Circle> list = circleDAO.getAllCircles();
			if (list.size() > 0) {
				return new ResponseEntity<List<Circle>>(list, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No User found in DB", HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<String>("UnAuthorized", HttpStatus.UNAUTHORIZED);
		}
	}

	/*
	 * Define a handler method which will retrieve all the available circles
	 * matching a search keyword. This handler method should return any one of the
	 * status messages basis on different situations: 1. 200(OK) - In case of
	 * success 2. 401(UNAUTHORIZED) - If the user is not logged in
	 * 
	 * This handler method should map to the URL "/api/circle/search/{searchString}"
	 * using HTTP GET method" where "searchString" should be replaced with the
	 * actual search keyword without the {}
	 */
	@GetMapping(path="/search/{searchString}")
	public ResponseEntity<?> getAllCircleNameBysearchString(@PathVariable("searchString") String searchString ,HttpSession session) {
		if (session.getAttribute("LoginUserName") != null) {
			List<Circle> list = circleDAO.getAllCircles(searchString);
			System.out.println("Matching circle Name list"+list);
			if (list.size() > 0) {
				return new ResponseEntity<List<Circle>>(list, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No User found in DB", HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<String>("UnAuthorized", HttpStatus.UNAUTHORIZED);
		}
	}

}
