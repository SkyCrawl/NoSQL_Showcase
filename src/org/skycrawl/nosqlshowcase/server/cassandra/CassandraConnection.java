package org.skycrawl.nosqlshowcase.server.cassandra;

import org.skycrawl.nosqlshowcase.server.root.db.AbstractDatabaseConnection;

public class CassandraConnection extends AbstractDatabaseConnection<Object, CassandraDataController>
{
	private static final long	serialVersionUID	= -4778024073075459382L;

	@Override
	protected Object doConnect(String hostname, int port) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected CassandraDataController createDataController(Object connection)
	{
		return new CassandraDataController(connection);
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