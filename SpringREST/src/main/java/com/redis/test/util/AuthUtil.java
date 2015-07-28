package com.redis.test.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * Ref: https://www.owasp.org/index.php/Hashing_Java
 * 
 * @author ketav
 */

public class AuthUtil {

	private AuthUtil(){}
	
	public static final AuthUtil instance = new AuthUtil();
	
	private final static int ITERATION_NUMBER = 10;
	
	public byte[] createSalt() throws NoSuchAlgorithmException{
		Random rand = SecureRandom.getInstance("SHA1PRNG");
		byte[] bsalt = new byte[20];
		rand.nextBytes(bsalt);
		return bsalt;
	}
	
	public String byteToBase64String(byte [] arr){
		return Base64.getEncoder().encodeToString(arr);
	}
	
	public byte[] base64StringtoByte(String str){
		return Base64.getDecoder().decode(str);
	}
	
	public byte[] getHash(String cleanTextPassword, byte[] salt) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(salt);
		byte[] input = digest.digest(cleanTextPassword.getBytes("UTF-8"));
		for (int i = 0; i < ITERATION_NUMBER; i++) {
			digest.reset();
			input = digest.digest(input);
		}
		return input;
	}
	
}
