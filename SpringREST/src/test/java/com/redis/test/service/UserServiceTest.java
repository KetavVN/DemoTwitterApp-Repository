package com.redis.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import com.redis.test.dao.UserDAO;
import com.redis.test.domain.User;

/**
 * @author Ketav
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserDAO userDAO;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	private User user;

	@Before
	public void setUp() throws Exception {
		user = new User();
		user.setEmail("test@test.com");
		user.setUserName("test");
	}

	@After
	public void tearDown() throws Exception {
		user = null;
	}

	@Test
	public void testFindUser() {
		when(userDAO.getUserByName("test")).thenReturn(user);
		assertEquals(user, userService.findUser("test"));
		
		when(userDAO.getUserByName("test")).thenReturn(null);
		assertEquals(null, userService.findUser("test"));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void testSaveUser() {
		when(userDAO.addUser(user)).thenReturn(user.getUserName());
		assertEquals(user.getUserName(), userService.saveUser(user));
		
		//test duplicate
		when(userDAO.addUser(user)).thenThrow(
				new DataIntegrityViolationException("Integrity contraint violation."
						+ "Primary key already exists."));
		userService.saveUser(user);
	}

	@Test
	public void testRemoveUser() {
		when(userDAO.deleteUser(user)).thenReturn(true);
		assertTrue(userService.removeUser(user));
		
		when(userDAO.deleteUser(user)).thenReturn(false);
		assertFalse(userService.removeUser(user));
	}
}
