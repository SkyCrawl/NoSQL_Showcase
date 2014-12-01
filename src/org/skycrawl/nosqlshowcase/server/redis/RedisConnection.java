package org.skycrawl.nosqlshowcase.server.redis;

import org.skycrawl.nosqlshowcase.server.root.db.AbstractDatabaseConnection;

public class RedisConnection extends AbstractDatabaseConnection<Object, RedisDataController>
{
	private static final long	serialVersionUID	= -7994070487275846229L;

	@Override
	protected Object doConnect(String hostname, int port) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected RedisDataController createDataController(Object connection)
	{
		return new RedisDataController(connection);
	}
	
	@Override
	public String getDBVersion()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected boolean isAlive()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void doClose()
	{
		// TODO Auto-generated method stub
	}
}