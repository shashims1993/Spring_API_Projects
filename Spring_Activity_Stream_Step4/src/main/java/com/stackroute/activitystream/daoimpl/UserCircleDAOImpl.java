package com.stackroute.activitystream.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.activitystream.dao.CircleDAO;
import com.stackroute.activitystream.dao.UserCircleDAO;
import com.stackroute.activitystream.dao.UserDAO;
import com.stackroute.activitystream.model.Circle;
import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.model.UserCircle;

/*
* This class is implementing the UserCircleDAO interface. This class has to be annotated with 
* @Repository annotation.
* @Repository - is an annotation that marks the specific class as a Data Access Object, 
* thus clarifying it's role.
* @Transactional - The transactional annotation itself defines the scope of a single database 
* 					transaction. The database transaction happens inside the scope of a persistence 
* 					context.  
* */
@Repository("userCircleDAO")
@Transactional
public class UserCircleDAOImpl implements UserCircleDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.
	 */

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CircleDAO circleDAO;
	@Autowired
	UserCircle userCircle;

	/*
	 * Add a user to a circle
	 */
	public boolean addUser(String username, String circleName) {

		UserCircle userCircle = new UserCircle();
		userCircle.setUsername(username);
		userCircle.setCircleName(circleName);
		Boolean flag=true;
		try {
		if (circleDAO.get(circleName) != null && userDAO.get(username) != null) {
			System.out.println("User Name and circle name exists");
			if (get(username, circleName) == null) {
				System.out.println("User is not added to the circle so adding");
				sessionFactory.getCurrentSession().save(userCircle);
				sessionFactory.getCurrentSession().flush();
				flag=true;
			} 
		} else {
			flag=false;
		}
		
	}catch(Exception e)
		{
		System.out.println(e.getMessage());
		}
		return flag;
	}

	/*
	 * Remove a user from a circle
	 */

	public boolean removeUser(String username, String circleName) {

		Boolean flag = false;
		try {
			userCircle = get(username, circleName);
			if (userCircle != null) {
				userCircle.setUsername(username);
				userCircle.setCircleName(circleName);
				System.out.println("Succesfully Deleted User in UserCircleDAOImpn");
				sessionFactory.getCurrentSession().delete(userCircle);
				sessionFactory.getCurrentSession().flush();
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			flag = false;

		}
		return flag;
	}
	/*
	 * public boolean removeUser(String username, String circleName) {
	 * System.out.println("Called Delete function in UserCircleDAOImpn"); boolean
	 * userFlag = userDAO.exists(username); Circle circleFlag =
	 * circleDAO.get(circleName);
	 * 
	 * if (userFlag && circleFlag != null) { userCircle.setUsername(username);
	 * userCircle.setCircleName(circleName);
	 * System.out.println("Succesfully Deleted User in UserCircleDAOImpn");
	 * sessionFactory.getCurrentSession().delete(userCircle);
	 * sessionFactory.getCurrentSession().flush(); return true; } else { return
	 * false; } }
	 */

	/*
	 * Retrieve unique UserCircle object which contains a specific username and
	 * circleName
	 */
	/*
	 * public UserCircle get(String username, String circleName) { try {
	 * 
	 * @SuppressWarnings("unchecked") Query<UserCircle> query =
	 * sessionFactory.getCurrentSession()
	 * .createQuery("from UserCircle where username=:username and circleName=:circleName"
	 * ); query.setParameter("username", username); query.setParameter("circleName",
	 * circleName); UserCircle userCircle = query.uniqueResult();
	 * System.out.println(userCircle); return userCircle; } catch (Exception e) {
	 * System.out.println("Error while retreiving the user circle by name"); return
	 * null; } }
	 */
	public UserCircle get(String username, String circleName) {
		String hql = "from UserCircle where username= '" + username + "' and circleName= '" + circleName + "'";
		UserCircle usercircle = (UserCircle) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
		return usercircle;
	}

	/*
	 * Retrieve all subscribed circles by a user
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMyCircles(String username) {
		String hql = "SELECT U.circleName FROM UserCircle U where U.username=  '" + username + "' ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<String> list = query.list();
		return list;
	}

}
