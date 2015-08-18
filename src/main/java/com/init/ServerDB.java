package com.init;

import com.cache.JedisTool;
import com.cache.RedisMap;
import com.configparse.ConfigXmlParse;
import com.configparse.vo.ConfigProject;
import com.configparse.vo.ConfigRedis;
import com.db.DbConnection;
import com.util.KeyRule;

public class ServerDB {
	// private static Logger LOGGER = LoggerFactory.getLogger(ServerDB.class);

	public ServerDB(String filename) throws Exception {
		ConfigProject configProject = ConfigXmlParse.annotationParse(filename);

		if (configProject != null && configProject.getTables().size() > 0) {
			initRedis(configProject.getConfigRedis());
			DbConnection.loadTable(configProject);
		}
	}

	public void initRedis(ConfigRedis configRedis) throws Exception {
		JedisTool.maxTotal = configRedis.getMaxTotal();
		JedisTool.maxIdle = configRedis.getMaxIdle();
		JedisTool.maxWait = configRedis.getMaxWait();
		JedisTool.testOnBorrow = false;
		JedisTool.init(configRedis.getHost(), configRedis.getPort(), configRedis.getPassword());
	}

	public static void main(String[] args) throws Exception {
		String file = "E:\\rick_workspace\\ServerDB\\src\\main\\resources\\dbconfig.xml";
		new ServerDB(file);

		System.out.println(RedisMap.hget(KeyRule.schemaTable("dzq_job", "company"), "10"));
	}
}
