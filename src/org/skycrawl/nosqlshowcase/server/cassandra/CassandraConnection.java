package org.skycrawl.nosqlshowcase.server.cassandra;

import org.skycrawl.nosqlshowcase.server.cassandra.controller.CassandraDataController;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDatabaseConnection;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class CassandraConnection extends AbstractDatabaseConnection<Session, CassandraDataController>
{
	private static final long	serialVersionUID	= -4778024073075459382L;

	@Override
	protected Session doConnect(String hostname, int port) throws Exception
	{
		return Cluster.builder().addContactPoint(hostname).build().connect();
	}
	
	@Override
	protected CassandraDataController createDataController(Session connection)
	{
		return new CassandraDataController(connection);
	}
	
	@Override
	public String getDBVersion()
	{
		return getConnection().getCluster().getMetadata().getAllHosts().iterator().next().getCassandraVersion().toString();
	}
	
	@Override
	protected boolean isAlive()
	{
		Metadata metadata = getConnection().getCluster().getMetadata();
		if(metadata != null)
		{
			return !metadata.getAllHosts().isEmpty();
		}
		else
		{
			return false;
		}
	}

	@Override
	protected void doClose()
	{
		getConnection().getCluster().close();
	}
}