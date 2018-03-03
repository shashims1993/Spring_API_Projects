package com.stackroute.activitystream.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.activitystream.dao.CircleDAO;
import com.stackroute.activitystream.dao.UserDAO;
import com.stackroute.activitystream.model.Circle;
import com.stackroute.activitystream.model.User;


/*
* This class is implementing the CircleDAO interface. This class has to be annotated with 
* @Repository annotation.
* @Repository - is an annotation that marks the specific class as a Data Access Object, 
* thus clarifying it's role.
* @Transactional - The transactional annotation itself defines the scope of a single database 
* 					transaction. The database transaction happens inside the scope of a persistence 
* 					context.  
* */
@Repository("circleDAO")
@Transactional
public class CircleDAOImpl implements CircleDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory. 
	 */
	
	/*
	 * Autowiring should be implemented for UserDAO. 
	 */
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	UserDAO userDAO;
//	@Autowired
//	Session session;
		
	/*
	 * Create a new circle
	 */
	public boolean save(Circle circle) {
        // TODO Auto-generated method stub
        
        try{
            circle.setCreatedDate();
            //if not check test case will fail
            if(userDAO.get(circle.getCreatorId())!=null)
            {
                
                sessionFactory.getCurrentSession().save(circle);
                sessionFactory.getCurrentSession().flush();
                return true;
            }
            
            else
                return false;
        
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
	/*public boolean save(Circle circle) {
		try {
			System.out.println("Called Save function in CircleDAOImpn");
			sessionFactory.getCurrentSession().save(circle);
			sessionFactory.getCurrentSession().flush();
			return true;
		} catch (Exception e) {
			System.out.println("Error while saving"+e.getMessage());
			return false;
		}
	}*/

	/*
	 * Update an existing circle
	 */
	public boolean update(Circle circle) {
		try {
			System.out.println("Called Update function in CircleDAOImpn");
			sessionFactory.getCurrentSession().update(circle);
			sessionFactory.getCurrentSession().flush();
			return true;
		} catch (Exception e) {
			System.out.println("Error while saving"+e.getMessage());
			return false;
		}
	}

	
	/*
	 * delete an existing circle
	 */
	public boolean delete(Circle circle) {
		try {
			System.out.println("Called Delete function in CircleDAOImpn");
			sessionFactory.getCurrentSession().delete(circle);
			sessionFactory.getCurrentSession().flush();
			return true;
		} catch (Exception e) {
			System.out.println("Error while saving"+e.getMessage());
			return false;
		}
	}


	/*
	 * Retrieve a specific circle
	 */
	public Circle get(String circleName) {
		try {
			@SuppressWarnings("unchecked")
			Query<Circle> query = sessionFactory.getCurrentSession().createQuery("from Circle where circleName= :circleName");
			query.setParameter("circleName", circleName);
			Circle circle = query.getSingleResult();
			return circle;
		} catch(Exception e) {
			System.out.println("Error while retreiving the circle by name");
			return null;
		}

	}
	
	
	
	/*
	 * retrieving all circles
	 */
	public List<Circle> getAllCircles() {
		String hql = "from Circle";
		return (List<Circle>) sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	
	/*
	 * Retrieving all circles that matches a search string
	 */
	@SuppressWarnings("unchecked")
	public List<Circle> getAllCircles(String searchString) {
		Session session = sessionFactory.openSession();
		 Criteria cr = session.createCriteria(Circle.class);
	        cr.add(Restrictions.ilike("circleName", "%searchString%"));
	        List<Circle> list= cr.list();
	        return list;
		

	}	

}
