package com.redis.test.controller;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.support.BindingAwareModelMap;

import com.redis.test.domain.User;
import com.redis.test.web.AppContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppContext.class, WebAppConfiguration.class})
@Transactional
public class HomeControllerTest {

	@Inject
	private HomeController homeController;
	
	private MockHttpSession session;
	private Model model;
	
	@Before
	public void setUp() throws Exception {
		//with mock context, mock id
		session = new MockHttpSession(null, null);
		model = new BindingAwareModelMap();
	}
	
	@After
	public void tearDown() throws Exception{
		session = null;
		model = null;
	}

	@Test
	public void testDisplayWelcomePage() {
		assertEquals("home", homeController.displayWelcomePage(model, session));
		session.setAttribute("userName", "ketav");
		assertEquals("welcome", homeController.displayWelcomePage(model, session));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testAddUser() {
		User user = new User();
		user.setUserName("test");
		user.setEmail("test@test.com");
		user.setPassphrase("test");
		model.addAttribute("user", user);
		MapBindingResult bindingResults = new MapBindingResult(new HashMap(),"user");
		
		assertEquals("redirect:/welcome", homeController.addUser(user, bindingResults, model, session));

		/*a validator must be registered with controller in order for input validations to work
		assertTrue(bindingResults.hasErrors());
		
		user.setUserName("test12");
		user.setPassphrase("test12");
		
		bindingResults = new MapBindingResult(new HashMap(),"user");
		
		assertEquals("welcome", homeController.addUser(user, 
				bindingResults, model, session));*/
		
	}

	@Test
	public void testRemoveUser() {
		session.setAttribute("userName", "test");
		assertEquals("redirect:/home", homeController.removeUser(model, session));
	}

}
