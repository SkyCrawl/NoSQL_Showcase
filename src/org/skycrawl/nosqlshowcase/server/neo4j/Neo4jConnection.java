package org.skycrawl.nosqlshowcase.server.neo4j;

import org.skycrawl.nosqlshowcase.server.root.db.AbstractDatabaseConnection;

public class Neo4jConnection extends AbstractDatabaseConnection<Object, Neo4jDataController>
{
	private static final long	serialVersionUID	= 953686838975338432L;

	@Override
	protected Object doConnect(String hostname, int port) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected Neo4jDataController createDataController(Object connection)
	{
		return new Neo4jDataController(connection);
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