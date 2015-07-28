package com.redis.test.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.redis.test.annotations.RCached;
import com.redis.test.dao.UserDAO;
import com.redis.test.domain.User;
import com.redis.test.util.AuthUtil;

/**
 * User service implementation
 * 
 * @author Ketav
 */

@Named
@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
	
	@Inject @Named("hibernateUserDAO")
	private UserDAO userDAO;
	
	private static final String mapName = "users";
	
	@Override @RCached(key=mapName)
	public User findUser(String userName) {
		return userDAO.getUserByName(userName);
	}

	@Override @RCached(key=mapName)
	public String saveUser(User user) {
		try {
			encrypt(user);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new SecurityException(e.getMessage());
		}
		return userDAO.addUser(user);
	}

	private void encrypt(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte [] salt = AuthUtil.instance.createSalt();
		user.setSalt(AuthUtil.instance.byteToBase64String(salt));
		
		byte [] hash = AuthUtil.instance.getHash(user.getPassphrase(), salt);
		user.setPassphrase(AuthUtil.instance.byteToBase64String(hash));
	}
	
	@Override @RCached(key=mapName)
	public boolean removeUser(User user){
		return userDAO.deleteUser(user);
	}
	
	@Override
	public boolean authenticate(String userName, String clearTextPassword) {
		String salt = null;
		String digest = null;
		boolean userExist = false;
		
		User user = userDAO.getUserByName(userName);
		if(user!=null){
			userExist = true;
			salt = user.getSalt();
			digest = user.getPassphrase();
		} else {
			digest = "000000000000000000000000000=";
            salt = "00000000000=";
		}
		
		byte [] bsalt = AuthUtil.instance.base64StringtoByte(salt);
		byte [] proposedPassCodeHash = null;
		
		try {
			proposedPassCodeHash = AuthUtil.instance.getHash(clearTextPassword, bsalt);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new SecurityException(e.getMessage());
		}
		
		String hashPassCode = AuthUtil.instance.byteToBase64String(proposedPassCodeHash);
		
		return digest.equals(hashPassCode) && userExist;
	}
	
}
