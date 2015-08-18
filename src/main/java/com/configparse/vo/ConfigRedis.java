package com.configparse.vo;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;

import com.util.ClassUtil;

@ObjectCreate(pattern = "project/redis")
public class ConfigRedis {
	@BeanPropertySetter(pattern = "project/redis/host")
	private String host;
	@BeanPropertySetter(pattern = "project/redis/port")
	private int port;
	@BeanPropertySetter(pattern = "project/redis/password")
	private String password;
	@BeanPropertySetter(pattern = "project/redis/maxTotal")
	private int maxTotal;
	@BeanPropertySetter(pattern = "project/redis/maxIdle")
	private int maxIdle;
	@BeanPropertySetter(pattern = "project/redis/maxWait")
	private long maxWait;
	@BeanPropertySetter(pattern = "project/redis/testOnBorrow")
	private boolean testOnBorrow;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public String toString() {
		return ClassUtil.getToString(this, "redis");
	}

}
