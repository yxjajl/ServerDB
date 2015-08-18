package com.configparse.vo;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;

import com.util.ClassUtil;

@ObjectCreate(pattern = "project/db")
public class ConfigDB {
	@BeanPropertySetter(pattern = "project/db/url")
	private String url;
	
	@BeanPropertySetter(pattern = "project/db/username")
	private String username;
	
	@BeanPropertySetter(pattern = "project/db/password")
	private String password;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString() {
		return ClassUtil.getToString(this, "db");
	}
	
}
