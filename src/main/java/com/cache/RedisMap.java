package com.cache;

import java.util.List;

import redis.clients.jedis.ShardedJedis;

public class RedisMap {
	/**
	 * 添加一个对应关系
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid
	 * @param String
	 *            value
	 * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
	 * **/
	public static long hset(String key, String fieid, String value) {
		ShardedJedis sharding = JedisTool.getResource();
		long s = sharding.hset(key, fieid, value);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 从hash中删除指定的存储
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 状态码，1成功，0失败
	 * */
	public static long hdel(String key, String fieid) {
		ShardedJedis sharding = JedisTool.getResource();
		long s = sharding.hdel(key, fieid);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 测试hash中指定的存储是否存在
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 1存在，0不存在
	 * */
	public static boolean hexists(String key, String fieid) {
		ShardedJedis sharding = JedisTool.getResource();
		boolean s = sharding.hexists(key, fieid);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 返回hash中指定存储位置的值
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 存储对应的值
	 * */
	public static String hget(String key, String fieid) {
		ShardedJedis sharding = JedisTool.getResource();
		String s = sharding.hget(key, fieid);
		JedisTool.returnJedis(sharding);
		return s;
	}

	public static List<String> hvals(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		List<String> s = sharding.hvals(key);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 获取hash中存储的个数，类似Map中size方法
	 * 
	 * @param String
	 *            key
	 * @return long 存储的个数
	 * */
	public static long hlen(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long len = sharding.hlen(key);
		JedisTool.returnJedis(sharding);
		return len;
	}
}