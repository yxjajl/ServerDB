package com.cache;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class JedisTool {
	public static int maxTotal = 30;
	public static int maxIdle = 100;
	public static long maxWait = 10000L;
	public static boolean testOnBorrow = false;
	public static ShardedJedisPool pool;

	public static void init(String host, int port, String password) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWait);
		config.setTestOnBorrow(testOnBorrow);

		JedisShardInfo jsi = new JedisShardInfo(host, 6379);
		if (StringUtils.isNotEmpty(password)) {
			jsi.setPassword(password);
		}
		List<JedisShardInfo> shards = Arrays.asList(jsi);
		pool = new ShardedJedisPool(config, shards);
	}

	public static ShardedJedis getResource() {
		ShardedJedis shardedJedis = pool.getResource();
		return shardedJedis;
	}

	public static String get(String key) {
		ShardedJedis sharding = getResource();

		String value = sharding.get(key);
		returnJedis(sharding);
		return value;
	}

	// public static int getInt(String key) {
	// String num = get(key);
	// if (condition) {
	//
	// }
	// return Integer.parseInt(get(key));
	// }

	public static String set(String key, String value) {
		ShardedJedis sharding = JedisTool.getResource();
		String status = sharding.set(key, value);
		returnJedis(sharding);
		return status;
	}

	/**
	 * 增量,已经被强转为int
	 * 
	 * @param key
	 * @return
	 */
	public static int incr(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long count = sharding.incr(key);
		returnJedis(sharding);
		return (int) count;

	}

	public static int add(String key, int num) {
		ShardedJedis sharding = JedisTool.getResource();
		long count = sharding.incrBy(key, num);
		returnJedis(sharding);
		return (int) count;

	}

	public static long del(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long count = sharding.del(key);
		returnJedis(sharding);
		return count;
	}

	/**
	 * 回收jedis
	 * 
	 * @param jedis
	 */
	@SuppressWarnings("deprecation")
	public static void returnJedis(ShardedJedis sharding) {
		pool.returnResource(sharding);
	}


	/**
	 * key: PH_playerId <br/>
	 * 2014年7月23日
	 * 
	 * @author dingqu-pc100
	 */
	public static String createCombinedKey(String redisKey, int playerId) {
		return redisKey + playerId;
	}

	public static void main(String[] args) throws Exception {
		init("127.0.0.1", 6379, "");
		set("ak", "av");
		System.out.println(get("ak"));
		// jsi.createResource().flushAll()
	}

}
