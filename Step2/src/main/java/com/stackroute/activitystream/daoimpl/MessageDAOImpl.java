package com.stackroute.activitystream.daoimpl;

import java.util.List;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.activitystream.dao.MessageDAO;
import com.stackroute.activitystream.model.Message;

/*
 * This class is implementing the MessageDAO interface. This class has to be annotated with @Repository
 * annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, thus 
 * 				 clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */

@Repository
@Transactional
public class MessageDAOImpl implements MessageDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory. 
	 */
	
//	private DataSource dataSource;
//	private SessionFactory sessionFactory;

	@Autowired
	SessionFactory sessionFactory;
	
	
	Session session ;
	

	/*
	 * retrieve all existing messages sorted by posted Date in descending order(showing latest
	 * message first)
	 */
	@Override
	public List<Message> getMessages() {
		// TODO Auto-generated method stub
session = sessionFactory.openSession();
		
		Query query = session.createQuery("from Message order by PostedDate desc");
		
		
		List<Message> list = query.list();
		System.out.println("list data " +list);
		
		session.close();
		return list;
	}


	/*
	 * Save the message in the database in message table 
	 */
	@Override
	public boolean sendMessage(Message message) {
		System.out.println("Called Send Mesage :Inserted one data");
		//session = HibernateUtil.getSessionFactory().openSession();
		
		session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(message);//insert query gets fired
		session.getTransaction().commit();
		
		session.close();
		
		return true;
		
	}
	
	/*
	 * Remove the message from the database in message table. Please note that this
	 * method is not required for implementing the functionality but is required for passing
	 * the test cases
	 */

	@Override
	public boolean removeMessage(Message message) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
