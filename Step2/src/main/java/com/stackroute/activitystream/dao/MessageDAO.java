package com.stackroute.activitystream.dao;

import java.util.List;

import com.stackroute.activitystream.model.Message;

public interface MessageDAO {
		
		public List<Message> getMessages(); 
		public boolean sendMessage(Message message);
		public boolean removeMessage(Message message);
		 
		

}
