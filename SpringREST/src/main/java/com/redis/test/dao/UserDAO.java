package com.redis.test.dao;

import org.hibernate.Session;

import com.redis.test.domain.User;

/**
 * dao interface for user
 * 
 * @author Ketav
 */

public interface UserDAO {
	public Session getCurrentSession();
	public User getUserByName(String userName);
	public String addUser(User user);
	public boolean deleteUser(User user);
}
