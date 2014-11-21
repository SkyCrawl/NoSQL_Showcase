package org.skycrawl.nosqlshowcase.redis;

import org.skycrawl.nosqlshowcase.ui.MyDBConnection;

import redis.clients.jedis.Jedis;

public class RedisConnection extends MyDBConnection
{
	private Jedis redisClient;
	
	public RedisConnection()
	{
		this.redisClient = null;
	}
	
	@Override
	protected void connectPrivate(String hostname, int port) throws Exception
	{
		if(redisClient != null)
		{
			destroyCurrentSession();
		}
		
		redisClient = new Jedis(hostname);
	}
	
	@Override
	protected void destroyCurrentSession()
	{
		redisClient.shutdown();
		redisClient = null;
	}
	
	// --------------------------------------------------------------------------------------------
	// MAIN DB OPERATIONS
	
	
	
	// --------------------------------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	
}
