package com.redis.test.service;

import com.redis.test.domain.User;


/**
 * User service
 * 
 * @author Ketav
 */

public interface UserService {
	public User findUser(String userName);
	public String saveUser(User user);
	public boolean removeUser(User user);
	public boolean authenticate(String userName, String clearTextPassword);
}
