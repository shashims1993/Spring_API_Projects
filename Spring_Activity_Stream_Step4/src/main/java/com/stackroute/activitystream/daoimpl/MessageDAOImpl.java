package com.stackroute.activitystream.daoimpl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.activitystream.dao.CircleDAO;
import com.stackroute.activitystream.dao.MessageDAO;
import com.stackroute.activitystream.dao.UserCircleDAO;
import com.stackroute.activitystream.dao.UserDAO;
import com.stackroute.activitystream.model.Message;
import com.stackroute.activitystream.model.UserTag;

/*
* This class is implementing the MessageDAO interface. This class has to be annotated with 
* @Repository annotation.
* @Repository - is an annotation that marks the specific class as a Data Access Object, 
* thus clarifying it's role.
* @Transactional - The transactional annotation itself defines the scope of a single database 
* 					transaction. The database transaction happens inside the scope of a persistence 
* 					context.  
* */
@Repository("messageDAO")
@Transactional
public class MessageDAOImpl implements MessageDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.
	 */
	@Autowired
	SessionFactory sessionFactory;

	/*
	 * Autowiring should be implemented for CircleDAO
	 */
	@Autowired
	CircleDAO circleDAO;

	/*
	 * Autowiring should be implemented for UserDAO.
	 */
	@Autowired
	UserDAO userDAO;

	/*
	 * Autowiring should be implemented for UserCircleDAO.
	 */
	@Autowired
	UserCircleDAO userCircleDAO;

	@Autowired
	Message message;
	
	@Autowired
	UserTag userTag;
	private int pageSize = 5;

	/*
	 * Retrieve messages from a specific circle. For improved performace, we will
	 * implement retrieving the messages partially by implementing pagination
	 */
	public List<Message> getMessagesFromCircle(String circleName, int pageNumber) {
		Query query = sessionFactory.getCurrentSession()
				.createQuery("from Message where circleName=? order by postedDate desc").setString(0, circleName);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageNumber);
		return query.list();
	}

	/*
	 * Retrieve messages between two users. Please note that in a one to one
	 * conversation, both users can act sometimes as a sender and sometimes as a
	 * recipient. For improved performace, we will implement retrieving the messages
	 * partially by implementing pagination
	 */
	public List<Message> getMessagesFromUser(String username, String otherUsername, int pageNumber) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from Message where (senderName=? and receiverId=?) or (senderName=? and receiverId=?) order by postedDate desc")
				.setString(0, username).setString(1, otherUsername).setString(2, otherUsername).setString(3, username);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageNumber);
		return query.list();
	}

	/*
	 * Retrieve messages from all circles subscribed by a specific user. For
	 * improved performace, we will implement retrieving the messages partially by
	 * implementing pagination
	 */
	public List<Message> getMessages(String username, int pageNumber) {
		List<Message> messageFromCircle = null;
		List<Message> allMessages = new ArrayList<>();
		List<String> listOfCircle = userCircleDAO.getMyCircles(username);
		for (String circleName : listOfCircle) {
			messageFromCircle = getMessagesFromCircle(circleName, pageNumber);
			if (messageFromCircle != null) {
				allMessages.addAll(messageFromCircle);
			}
		}
		return allMessages;

	}

	/*
	 * send messages from a specific circle. The posted message should have the
	 * current timestamp as the posted timestamp.
	 */
	public boolean sendMessageToCircle(String circleName, Message message) {
		if (circleDAO.get(circleName) != null && userDAO.get(message.getSenderName()) != null) {
			message.setCircleName(circleName);
			message.setPostedDate();
			sessionFactory.getCurrentSession().save(message);
			sessionFactory.getCurrentSession().flush();
			return true;
		} else {
			return false;
		}

	}

	/*
	 * Send message to a specific user
	 */
	public boolean sendMessageToUser(String username, Message message) {
		if (userDAO.get(message.getSenderName()) != null && userDAO.get(username) != null) {

			message.setPostedDate();
			sessionFactory.getCurrentSession().save(message);
			sessionFactory.getCurrentSession().flush();
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Retrieve all the tags available in the messages
	 */
	public List<String> listTags() {
		String hql = "SELECT M.tag FROM Message M";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<String> list = query.list();
		return list;
	}

	/*
	 * Retrieve all tags subscribed by a user
	 */
	public List<String> listMyTags(String username) {
		String hql = "SELECT M.tag FROM Message M where receiverId= " + username;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<String> list = query.list();
		return list;
	}

	/*
	 * Retrieve all messages containing a specific tag. For improved performace, we
	 * will implement retrieving the messages partially by implementing pagination
	 */
	public List<Message> showMessagesWithTag(String tag, int pageNumber) {
		Query query = sessionFactory.getCurrentSession()
				.createQuery("from Message where tag=? order by postedDate desc").setString(0, tag);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageNumber);
		return query.list();
	}

	/*
	 * Subscribe user to a tag. Please implement validation to check whether the
	 * user and tag both exists.
	 */
	public boolean subscribeUserToTag(String username, String tag) {
		String hql = "SELECT M.tag FROM Message M where tag= " + tag;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<String> list = query.list();
		Boolean flag=false;
		if(list.size()>0 && userDAO.get(username)!=null)
		{
			userTag.setUsername(username);
			userTag.setTag(tag);
			sessionFactory.getCurrentSession().save(userTag);
			sessionFactory.getCurrentSession().flush();
			flag=true;
		}
		return flag;
			
	}

	/*
	 * Unsubscribe a user from a tag. Please implement validation to check whether
	 * the user has subscribed to the tag or not
	 */
	public boolean unsubscribeUserToTag(String username, String tag) {
		String hql = "SELECT M.tag FROM Message M where tag= " + tag;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<String> list = query.list();
		Boolean flag=false;
		if(list.size()>0 && userDAO.get(username)!=null)
		{
			userTag.setUsername(username);
			userTag.setTag(tag);
			sessionFactory.getCurrentSession().delete(userTag);
			sessionFactory.getCurrentSession().flush();
			flag=true;
		}
		return flag;

	}

	/*
	 * Retrieve UserTag object for a username and a tag
	 */
	public UserTag getUserTag(String username, String tag) {
		// TODO Auto-generated method stub
		return null;
	}

}
