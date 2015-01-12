package org.skycrawl.nosqlshowcase.server.neo4j;

import org.skycrawl.nosqlshowcase.server.neo4j.controller.Neo4jDataController;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDatabaseConnection;

public class Neo4jConnection extends AbstractDatabaseConnection<Neo4jQueryWrapper, Neo4jDataController>
{
	private static final long	serialVersionUID	= 953686838975338432L;
	
	@Override
	protected Neo4jQueryWrapper doConnect(String hostname, int port) throws Exception
	{
		return new Neo4jQueryWrapper();
	}
	
	@Override
	protected Neo4jDataController createDataController(Neo4jQueryWrapper connection)
	{
		// return different types to try out different implementations
		return new Neo4jDataController(connection);
	}
	
	@Override
	public String getDBVersion()
	{
		return getConnection().getDBVersion();
	}
	
	@Override
	protected boolean isAlive()
	{
		return getConnection().isAlive();
	}

	@Override
	protected void doClose()
	{
		getConnection().close();
	}
}