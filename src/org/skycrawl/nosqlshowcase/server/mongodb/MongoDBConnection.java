package org.skycrawl.nosqlshowcase.server.mongodb;

import org.skycrawl.nosqlshowcase.server.root.db.AbstractDatabaseConnection;

public class MongoDBConnection extends AbstractDatabaseConnection<Object, MongoDBDataController>
{
	private static final long	serialVersionUID	= -2059812867468336138L;

	@Override
	protected Object doConnect(String hostname, int port) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected MongoDBDataController createDataController(Object connection)
	{
		return new MongoDBDataController(connection);
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