package org.skycrawl.nosqlshowcase.server.redis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.skycrawl.nosqlshowcase.server.redis.controller.RedisDataController;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDatabaseConnection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection extends AbstractDatabaseConnection<JedisPool, RedisDataController>
{
	private static final long	serialVersionUID	= -7994070487275846229L;
	
	/*
	 * Jedis instances are not thread-safe and jedis connection pools should be used instead (see below). Jedis
	 * pool is threadsafe and can be used to reliably create several Jedis instances which should be returned
	 * to the pool when done.
	 * 
	 * Jedis implements Closable. Hence, the jedis instance will be auto-closed after the last statement:
	 * try (Jedis jedis = pool.getResource())
	 * {
	 * 		jedis.set("foo", "bar");
	 * 		String foobar = jedis.get("foo");
	 * 		jedis.zadd("sose", 0, "car"); jedis.zadd("sose", 0, "bike");
	 * 		Set<String> sose = jedis.zrange("sose", 0, -1);
	 * }
	 */

	@Override
	protected JedisPool doConnect(String hostname, int port) throws Exception
	{
		// getConnection().initPool(poolConfig, factory);
		return new JedisPool(new JedisPoolConfig(), hostname);
	}
	
	@Override
	protected RedisDataController createDataController(JedisPool connection)
	{
		return new RedisDataController(connection);
	}

	@Override
	public String getDBVersion()
	{
		try (Jedis jedis = getConnection().getResource())
		{
			String serverInfo = jedis.info("Server");
			Matcher m = Pattern.compile("redis_version:([\\d\\.]+)").matcher(serverInfo);
			if (m.find())
			{
				return m.group(1);
			}
			else
			{
				return null;
			}
		}
	}
	
	@Override
	protected boolean isAlive()
	{
		try (Jedis jedis = getConnection().getResource())
		{
			// jedis.ping();
			return jedis.isConnected();
		}
	}

	@Override
	protected void doClose()
	{
		getConnection().destroy();
	}
}