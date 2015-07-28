package com.redis.test.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.redis.test.annotations.RCacheable;

/**
 * 
 * @author ketav
 */

@Entity @Table(name="users")
@RCacheable(keyField="userName", timeOut=120)
public class User implements Serializable {

	private static final long serialVersionUID = -3333102629324681103L;
	
	public static final String unameMessage = "Username must be within 6 to 20 characters.";
	public static final String passwordMessage = "Password must be within 6 to 20 characters.";
	public static final String emailMessage = "Email must be in proper format.";
	public static final String emailPattern = "^[a-zA-Z0-9._]+@[a-zA-Z0-9]+.[a-zA-Z]{2,4}$";

	@Id
	@Size(min=6, max=20, message=unameMessage)
	private String userName;

	@Column(length=100)
	@Size(min=6, max=100, message=passwordMessage)
	private String passphrase;
	
	@Column
	private Integer age;
	
	@Column
	private String avatar;
	
	@Column
	@Pattern(regexp=emailPattern, message=emailMessage)
	private String email;
	
	@Column @JsonIgnore
	private String salt;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassphrase() {
		return passphrase;
	}
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
}
