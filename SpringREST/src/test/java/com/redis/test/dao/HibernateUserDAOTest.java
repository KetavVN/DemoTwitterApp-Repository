package com.redis.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.redis.test.domain.User;
import com.redis.test.web.AppContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppContext.class)
@Transactional
public class HibernateUserDAOTest {

	@Inject @Named("hibernateUserDAO")
	private UserDAO userDAO;
	private User user;
	
	@Before
	public void setUp() throws Exception{
		user = new User();
		user.setAge(20);
		user.setEmail("test@test.com");
		user.setUserName("test");
	}
	
	@After
	public void cleanUp() throws Exception{
		userDAO.deleteUser(user);
	}
	
	@Test/*(expected=DataIntegrityViolationException.class)*/
	public void testAddUser2() throws Exception{
		user.setUserName("test12");
		user.setPassphrase("test12");
		assertEquals(user.getUserName(), userDAO.addUser(user));
		/*userDAO.getCurrentSession().flush();
		userDAO.addUser(user);
		userDAO.getCurrentSession().flush();*/
	}
	
	@Test
	public void testGetUserByName() {
		user.setUserName("test12");
		user.setPassphrase("test12");
		assertEquals(user.getUserName(), userDAO.addUser(user));
		assertEquals(user, userDAO.getUserByName(user.getUserName()));
	}

	@Test
	public void testDeleteUser() {
		user.setUserName("test12");
		user.setPassphrase("test12");
		assertEquals(user.getUserName(), userDAO.addUser(user));
		assertTrue(userDAO.deleteUser(user));
		//assertFalse(userDAO.deleteUser(user));
	}

}
