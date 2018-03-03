package com.stackroute.activitystream.test;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.stackroute.activitystream.config.ApplicationContextConfig;
import com.stackroute.activitystream.controller.MessageController;
import com.stackroute.activitystream.dao.MessageDAO;
import com.stackroute.activitystream.model.Message;

import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContextConfig.class})
@WebAppConfiguration
public class MessageControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private Message message;
	
	@Mock
	private MessageDAO messageDAO;
	
	@InjectMocks
	private MessageController messageController=new MessageController();
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc=MockMvcBuilders.standaloneSetup(messageController)
				.build();
			
	}
	
	@Test
    public void testGetAllMessages() throws Exception {
		
		Message firstMessage=new Message();
		firstMessage.setSenderName("John");
		firstMessage.setMessage("Sample message");
		firstMessage.setPostedDate();
		
		Message secondMessage=new Message();
		secondMessage.setSenderName("Chris");
		secondMessage.setMessage("Another Sample message");
		secondMessage.setPostedDate();
		
		when(messageDAO.getMessages()).thenReturn(Arrays.asList(firstMessage, secondMessage));
		

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("index"))
                .andExpect(model().attribute("messages", hasSize(2)))
                .andExpect(model().attribute("messages", hasItem(
                        allOf(
                                hasProperty("senderName", is("John")),
                                hasProperty("message", is("Sample message"))
                        )
                )))
                .andExpect(model().attribute("messages", hasItem(
                        allOf(
                                hasProperty("senderName", is("Chris")),
                                hasProperty("message", is("Another Sample message"))
                        )
                )));
                ;
 
    }
	
	
	@Test
    public void testSendMessage() throws Exception {
			
		when(messageDAO.sendMessage(message)).thenReturn(true);
        mockMvc.perform(post("/sendMessage")
        			.param("sender", "Tom")
        			.param("message", "How are you?"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                ;
        
    }
	
	
	@Test
    public void testSendMessageEmptyUsernameFailure() throws Exception {
			
		when(messageDAO.sendMessage(message)).thenReturn(true);
        mockMvc.perform(post("/sendMessage")
        			.param("sender", "")
        			.param("message", "How are you?"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("index"))
                ;
        
    }
	
	@Test
    public void testSendMessageEmptyMessageFailure() throws Exception {
			
		when(messageDAO.sendMessage(message)).thenReturn(true);
        mockMvc.perform(post("/sendMessage")
        			.param("sender", "Tom")
        			.param("message", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("index"))
                ;
    }
	
	@Test
    public void testSendMessageEmptySenderAndMessageFailure() throws Exception {
			
		when(messageDAO.sendMessage(message)).thenReturn(true);
        mockMvc.perform(post("/sendMessage")
        			.param("sender", "")
        			.param("message", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("index"))
                ;
        
    }
}
