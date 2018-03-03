package com.stackroute.activitystream.test;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.activitystream.config.JwtFilter;
import com.stackroute.activitystream.controller.UserAuthenticateController;
import com.stackroute.activitystream.model.User;
import com.stackroute.activitystream.service.UserService;
import com.stackroute.activitystream.controller.UserCircleController;
import com.stackroute.activitystream.model.UserCircle;
import com.stackroute.activitystream.service.UserCircleService;
import com.stackroute.activitystream.service.UserCircleServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserCircleControllerTest {

	@Mock
	private User user;
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserAuthenticateController userAuthenticateController = new UserAuthenticateController();
	
	@InjectMocks
	private JwtFilter jwtFilter = new JwtFilter();
	
	private MockMvc userCircleMockMvc;
	private MockMvc userAuthMockMvc;
	
	
	@Mock
	private UserCircleService userCircleService;
	
	@InjectMocks
	private UserCircleController userCircleController=new UserCircleController();
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		userCircleMockMvc=MockMvcBuilders.standaloneSetup(userCircleController).addFilter(jwtFilter)
				.build();
	}
	
	@Test
    public void testAddUserToCircle() throws Exception {
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		when(userCircleService.get("john", "spring")).thenReturn(null);
		when(userCircleService.addUser("john", "spring")).thenReturn(true);
		
		userCircleMockMvc.perform(put("/api/usercircle/addToCircle/john/spring")
				.header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk());
          
        verify(userCircleService, times(1)).get("john", "spring");
        verify(userCircleService, times(1)).addUser("john", "spring");
    }
	
	@Test
    public void testAddUserToCircleDuplicateFailure() throws Exception {
		
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
	
		when(userCircleService.get("john", "spring")).thenReturn(userCircle);
		when(userCircleService.addUser("john", "spring")).thenReturn(true);
		
		userCircleMockMvc.perform(put("/api/usercircle/addToCircle/john/spring")
				.header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isConflict());
                
        verify(userCircleService, times(1)).get("john", "spring");
        verify(userCircleService, times(0)).addUser("john", "spring");
    }
	
	
	
	
	
	
	@Test
    public void testRemoveUserFromCircle() throws Exception {
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		when(userCircleService.get("john", "spring")).thenReturn(null);
		when(userCircleService.removeUser("john", "spring")).thenReturn(true);
		
		userCircleMockMvc.perform(put("/api/usercircle/removeFromCircle/john/spring")
				.header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk());
                
        verify(userCircleService, times(0)).get("john", "spring");
        verify(userCircleService, times(1)).removeUser("john", "spring");
    }
	
	@Test
    public void testRemoveUserFromCircleFailure() throws Exception {
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		
		when(userCircleService.get("john", "spring")).thenReturn(null);
		when(userCircleService.removeUser("john", "spring")).thenReturn(false);
		
		userCircleMockMvc.perform(put("/api/usercircle/removeFromCircle/john/spring")
				.header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isInternalServerError());
                
        
        verify(userCircleService, times(1)).removeUser("john", "spring");
    }
	
	
	@Test
    public void testUserSubscriptionToCircle() throws Exception {
		
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		
		when(userCircleService.getMyCircles("john")).thenReturn(Arrays.asList("Spring","Angular"));
		
		userCircleMockMvc.perform(get("/api/usercircle/searchByUser/john")
				.header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk());
               
        verify(userCircleService, times(1)).getMyCircles("john");
    }
	
	
	@Test
    public void testUserSubscriptionToCircleFailure() throws Exception {
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		
		when(userCircleService.getMyCircles("john")).thenReturn(Arrays.asList("Spring","Angular"));
		
        verify(userCircleService, times(0)).getMyCircles("john");
    }
	
	
     /*converts a Java object into JSON representation*/
     
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /*
	 * 
	 * Does authentication and return back token
	 */

	public String getToken() throws Exception {
		userAuthMockMvc = MockMvcBuilders.standaloneSetup(userAuthenticateController).build();
		String userName = "john";
		String password = "password";
		User user = new User(userName, userName, password);
		when(userService.get(userName)).thenReturn(null);
		when(userService.validate(userName, password)).thenReturn(true);

		MvcResult result = userAuthMockMvc
				.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user)))
				// .andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("user successfully logged in"))).andReturn();
		String responseOutput = result.getResponse().getContentAsString();
		String parseToken = "\"token\":\"";
		String token = responseOutput.substring(responseOutput.indexOf(parseToken) + parseToken.length(),
				responseOutput.length() - 2);
		System.out.println("Token:" + token);
		return token;
	}
}
