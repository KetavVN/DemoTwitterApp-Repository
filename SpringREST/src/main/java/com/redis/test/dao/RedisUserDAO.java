package com.redis.test.dao;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.redis.test.domain.User;

/**
 * Redis based UserDAO implementation
 * 
 * @author ketav
 */

@Repository("redisUserDAO")
public class RedisUserDAO implements UserDAO{
	
	@Autowired
	private RedisTemplate<String, User> redisTemplate;
	
	@Resource(name="redisTemplate")
	private HashOperations<String, String, User> hashOps;
	
	private static final String mapName = "users";
	
	@Override
	public User getUserByName(String userName) {
		return hashOps.get(mapName, userName);
	}

	@Override
	public String addUser(User user) {
		hashOps.put(mapName, user.getUserName(), user);
		return user.getUserName();
	}

	@Override
	public boolean deleteUser(User user) {
		hashOps.delete(mapName, user.getUserName());
		return hashOps.get(mapName, user.getUserName()) == null;
	}

	@Override
	public Session getCurrentSession() {
		return null;
	}

}
