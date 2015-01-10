package org.skycrawl.nosqlshowcase.server.mongodb;

import org.skycrawl.nosqlshowcase.server.mongodb.controller.MongoDBDataController;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDatabaseConnection;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBConnection extends AbstractDatabaseConnection<DB, MongoDBDataController>
{
	private static final long	serialVersionUID	= -2059812867468336138L;
	
	public static final String dbName = "nosql_showcase";
	
	@Override
	protected DB doConnect(String hostname, int port) throws Exception
	{
		return new MongoClient(hostname, port).getDB(dbName);
	}
	
	@Override
	protected MongoDBDataController createDataController(DB connection)
	{
		return new MongoDBDataController(connection);
	}
	
	@Override
	public String getDBVersion()
	{
		return getConnection().command("buildInfo").getString("version");
	}
	
	@Override
	protected boolean isAlive()
	{
		return getConnection().getMongo().getDatabaseNames() != null;
	}

	@Override
	protected void doClose()
	{
		getConnection().getMongo().close();
	}
}