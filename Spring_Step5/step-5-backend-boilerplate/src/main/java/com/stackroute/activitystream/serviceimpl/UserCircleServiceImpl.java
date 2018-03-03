package com.stackroute.activitystream.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.activitystream.model.UserCircle;
import com.stackroute.activitystream.repository.CircleRepository;
import com.stackroute.activitystream.repository.UserCircleRepository;
import com.stackroute.activitystream.repository.UserRepository;
import com.stackroute.activitystream.service.UserCircleService;

/*
* Service classes are used here to implement additional business logic/validation.
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn’t currently 
* provide any additional behavior over the @Component annotation, but it’s a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class UserCircleServiceImpl implements UserCircleService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	CircleRepository circleRepository;
	@Autowired
	UserCircleRepository userCircleRepository;
	// @Autowired
	// UserCircle userCircle;
	/*
	 * Autowiring should be implemented for the UserRepository, CircleRepository,
	 * UserCircleRepository. Please note that we should not create any object using
	 * the new keyword
	 */

	/*
	 * This method should be used to add a user to a specific circle. You need to
	 * validate whether the user and also the circle exist. Also, please check if
	 * the user is already added to the circle. Call the corresponding method of
	 * Respository interface.
	 * 
	 */
	public boolean addUser(String username, String circleName) {
		Boolean flag = true;

		if (circleRepository.exists(circleName) && userRepository.exists(username)) {
			if (get(username, circleName) == null) {
				UserCircle userCircle = new UserCircle();
				userCircle.setUsername(username);
				userCircle.setCircleName(circleName);
				userCircleRepository.save(userCircle);
				flag = true;
			} else {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/*
	 * This method should be used to remove a user from a specific circle. Please
	 * check if the user is added to the circle before trying to remove. Call the
	 * corresponding method of Respository interface.
	 * 
	 */
	public boolean removeUser(String username, String circleName) {
		UserCircle userCircle = new UserCircle();
		userCircle.setUsername(username);
		userCircle.setCircleName(circleName);
		userCircleRepository.delete(userCircle);
		/*
		 * if (userCircleRepository.getUsernameAndCircleName(username, circleName) !=
		 * null) { return false; } else { return true; }
		 */
		return true;
	}

	/*
	 * This method should be used to show circles subscribed by a specific user.
	 * Call the corresponding method of Respository interface.
	 * 
	 */
	public List<String> getMyCircles(String username) {

		return userCircleRepository.findCircleNameByUserName(username);
	}

	/*
	 * This method should be used to check whether a specific user has subscribed to
	 * a specific circle. Call the corresponding method of Respository interface.
	 * 
	 */
	public UserCircle get(String username, String circleName) {
		System.out.println("get usercircle by username and circle name");
		UserCircle userCircle = userCircleRepository.getUsernameAndCircleName(username, circleName);
		System.out.println(userCircle);
		return userCircle;
	}

}
