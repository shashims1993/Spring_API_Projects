package com.stackroute.activitystream.test;

import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.activitystream.config.ApplicationContextConfig;
import com.stackroute.activitystream.config.PersistenceJPAConfig;
import com.stackroute.activitystream.controller.UserAuthController;
import com.stackroute.activitystream.controller.UserController;
import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.repository.UserRepository;
import com.stackroute.activitystream.service.UserService;

import junit.framework.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContextConfig.class, PersistenceJPAConfig.class})
@WebAppConfiguration
public class UserControllerTest {

	private MockMvc userMockMvc;
	private MockMvc userAuthMockMvc;
	
	
	@Mock
	private User user;
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserController userController=new UserController();
	
	@InjectMocks
	private UserAuthController userAuthController=new UserAuthController();
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		userMockMvc=MockMvcBuilders.standaloneSetup(userController)
				.build();
		userAuthMockMvc=MockMvcBuilders.standaloneSetup(userAuthController)
				.build();
			
	}
	
	@Test
    public void testGetAllUsers() throws Exception {
		
		User user=new User("john","","password");
		
		
		when(userService.validate("john", "password")).thenReturn(true);
		when(userService.get("john")).thenReturn(user);
	
        HttpSession session=userAuthMockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.password", is("password")))
                .andReturn()
                .getRequest()
                .getSession();
        
        Assert.assertNotNull(session);
		
		List<User> users=Arrays.asList(
				new User("john","John","password"),
				new User("chris","Chris","password2"));
		
		when(userService.list()).thenReturn(users);
		
        userMockMvc.perform(get("/api/user").session((MockHttpSession)session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("john")))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[1].username", is("chris")))
                .andExpect(jsonPath("$[1].name", is("Chris")))
                .andExpect(jsonPath("$[1].password", is("password2")))
                ;
        verify(userService, times(1)).list(); 
    }
	
	@Test
    public void testGetAllUsersFailure() throws Exception {
		
		User user=new User("john","","password");
		
		when(userService.validate("john", "password2")).thenReturn(true);
		when(userService.get("john")).thenReturn(user);
	
        HttpSession session=userAuthMockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getRequest()
                .getSession();
        
        Assert.assertNotNull(session);
		
		List<User> users=Arrays.asList(
				new User("john","John","password"),
				new User("chris","Chris","password2"));
		
		when(userService.list()).thenReturn(users);
		
        userMockMvc.perform(get("/api/user").session((MockHttpSession)session))
                .andExpect(status().isUnauthorized());
         
    }
	
	
	@Test
    public void testGetSpecificUser() throws Exception {
		
		User user=new User("john","John","password");
		when(userService.validate("john", "password")).thenReturn(true);
		when(userService.get("john")).thenReturn(user);
	
        HttpSession session=userAuthMockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.password", is("password")))
                .andReturn()
                .getRequest()
                .getSession();
        
        Assert.assertNotNull(session);
		
        userMockMvc.perform(get("/api/user/john").session((MockHttpSession)session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.password", is("password")));
      
    }
	
	@Test
    public void testGetSpecificUserFailure() throws Exception {
		
		User user=new User("john","John","password");
		when(userService.validate("john", "password2")).thenReturn(true);
		when(userService.get("john")).thenReturn(user);
	
        HttpSession session=userAuthMockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getRequest()
                .getSession();
        
        Assert.assertNotNull(session);
		
        userMockMvc.perform(get("/api/user/john").session((MockHttpSession)session))
                .andExpect(status().isUnauthorized());
      
    }
	
	@Test
    public void testCreateNewUser() throws Exception {
		
		User user=new User("john","John","password");
		when(userService.get("john")).thenReturn(null);
	
        userMockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.password", is("password")));
    }
	
	@Test
	public void testCreateNewUserFailure() throws Exception {
		
		User user=new User("john","John","password");
		when(userService.get("john")).thenReturn(user);
	
        userMockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isConflict());
    }
	
	@Test
    public void testUpdateExistingUser() throws Exception {

		User user=new User("john","John","password");
		when(userService.validate("john", "password")).thenReturn(true);
		when(userService.get("john")).thenReturn(user);
	
        HttpSession session=userAuthMockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.password", is("password")))
                .andReturn()
                .getRequest()
                .getSession();
        
        Assert.assertNotNull(session);
		
		User updatedUser=new User("john","John","password2");
		when(userService.get("john")).thenReturn(user);
	
        userMockMvc.perform(put("/api/user/john").session((MockHttpSession)session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.password", is("password2")));
    }
	
	@Test
    public void testUpdateInvalidUser() throws Exception {

		User user=new User("john","John","password");
		when(userService.validate("john", "password")).thenReturn(true);
		when(userService.get("john")).thenReturn(user);
	
        HttpSession session=userAuthMockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.password", is("password")))
                .andReturn()
                .getRequest()
                .getSession();
        
        Assert.assertNotNull(session);
		
		User updatedUser=new User("john","John","password2");
		when(userService.get("john")).thenReturn(user);
	
        userMockMvc.perform(put("/api/user/john2").session((MockHttpSession)session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedUser)))
                .andExpect(status().isNotFound());
    }
	
	
	
    /*
     * converts a Java object into JSON representation
     */
     
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
