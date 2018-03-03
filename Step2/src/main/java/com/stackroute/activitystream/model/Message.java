package com.stackroute.activitystream.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

/*
 * The class "Message" will be acting as the data model for the message Table in the database. Please
 * note that this class is annotated with @Entity annotation. Hibernate will scan all package for 
 * any Java objects annotated with the @Entity annotation. If it finds any, then it will begin the 
 * process of looking through that particular Java object to recreate it as a table in your database.
 */
@Component 
@Entity
public class Message {

	/*
	 * This class should have four fields
	 * (messageId,senderName,postedDate,message). Out of these four fields, the
	 * field messageId should be auto-generated. This class should also contain
	 * the getters and setters for the fields. The value of postedDate should
	 * not be accepted from the user but should be always initialized with the
	 * system date
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int messageId;
	String senderName;
	Timestamp PostedDate;
	String message;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Timestamp getPostedDate() {
		return PostedDate;
	}

	public void setPostedDate() {
		this.PostedDate =  new Timestamp(System.currentTimeMillis());;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", senderName=" + senderName + ", postedDate=" + PostedDate
				+ ", message=" + message + "]";
	}

	/*
	 * public void setSenderName(String string) { // TODO Auto-generated method
	 * stub
	 * 
	 * }
	 * 
	 * public void setMessage(String string) { // TODO Auto-generated method
	 * stub
	 * 
	 * }
	 * 
	 * public void setPostedDate() { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * public String getSenderName() { // TODO Auto-generated method stub return
	 * null; }
	 * 
	 * public String getMessage() { // TODO Auto-generated method stub return
	 * null; }
	 */

}
