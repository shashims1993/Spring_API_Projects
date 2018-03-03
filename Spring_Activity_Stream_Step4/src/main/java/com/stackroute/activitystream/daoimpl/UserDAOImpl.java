package com.stackroute.activitystream.daoimpl;

import java.util.List;


import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.activitystream.dao.UserDAO;
import com.stackroute.activitystream.model.User;

/*
* This class is implementing the UserCircleDAO interface. This class has to be annotated with 
* @Repository annotation.
* @Repository - is an annotation that marks the specific class as a Data Access Object, 
* thus clarifying it's role.
* @Transactional - The transactional annotation itself defines the scope of a single database 
* 					transaction. The database transaction happens inside the scope of a persistence 
* 					context.  
* */
@Repository("userDAO")
@Transactional

public class UserDAOImpl implements UserDAO {
	/*
	 * Autowiring should be implemented for the SessionFactory.
	 */
	@Autowired
	SessionFactory sessionFactory;

	/*
	 * Create a new user
	 */
	public boolean save(User user) {
		try {
			System.out.println("Called Save function in UserDAOImpn");
			sessionFactory.getCurrentSession().save(user);
			sessionFactory.getCurrentSession().flush();
			return true;
		} catch (Exception e) {
			System.out.println("Error while saving user"+e.getMessage());
			return false;
		}
	}

	/*
	 * Update an existing user
	 */
	public boolean update(User user) {
		try {
			System.out.println("Called Update function in UserDAOImpn");
			sessionFactory.getCurrentSession().update(user);
			sessionFactory.getCurrentSession().flush();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/*
	 * Remove an existing user
	 */
	public boolean delete(User user) {
		try {
			System.out.println("Called Delete function in UserDAOImpn");
			sessionFactory.getCurrentSession().delete(user);
			sessionFactory.getCurrentSession().flush();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/*
	 * Retrieve all available user
	 */
	@SuppressWarnings("unchecked")
	public List<User> list() {
		String hql = "from User";
		return (List<User>) sessionFactory.getCurrentSession().createQuery(hql).list();

	}

	/*
	 * validate an user
	 */
	// @SuppressWarnings("deprecation")
	@SuppressWarnings("unchecked")
	public boolean validate(String id, String password) {
		System.out.println("Called Validate function");
		try {
		Query<User> query = sessionFactory.getCurrentSession()
				.createQuery("from User where username= :username and password= :password");
		query.setParameter("username", id);
		query.setParameter("password", password);
		query.uniqueResult();

		List<User> list = query.list();
		System.out.println(list);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
		}catch(Exception e)
		{
			System.out.println("Error while validating"+e.getMessage());
			return false;
			
		}

	}

	/*
	 * Retrieve details of an user
	 */
	public User get(String id) {

		if (exists(id)) {
			@SuppressWarnings("unchecked")
			Query<User> query = sessionFactory.getCurrentSession().createQuery("from User where username= :username");
			query.setParameter("username", id);
			User user = query.getSingleResult();
			return user;
		} else {
			return null;
		}

	}

	/*
	 * check whether a user exists with a given userId
	 */
	public boolean exists(String id) {
		try {
			Query<User> query = sessionFactory.getCurrentSession().createQuery("from User where username= :username");
			query.setParameter("username", id);
			User user = query.getSingleResult();
			System.out.println(user.getUsername());
			if (user != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
