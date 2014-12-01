package org.skycrawl.nosqlshowcase.server.riak;

import org.skycrawl.nosqlshowcase.server.riak.controller.RiakDataController;
import org.skycrawl.nosqlshowcase.server.root.db.AbstractDatabaseConnection;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.query.NodeStats;

public class RiakConnection extends AbstractDatabaseConnection<IRiakClient, RiakDataController>
{
	private static final long	serialVersionUID	= -5082629521031505500L;
	
	@Override
	protected IRiakClient doConnect(String hostname, int port) throws Exception
	{
		return RiakFactory.pbcClient(hostname, port);
	}
	
	@Override
	public String getDBVersion()
	{
		try
		{
			for(NodeStats stats : getConnection().stats())
			{
				return stats.riakCoreVersion();
			}
		}
		catch (RiakException e)
		{
		}
		return null;
	}
	
	@Override
	protected RiakDataController createDataController(IRiakClient connection)
	{
		return new RiakDataController(connection);
	}
	
	@Override
	public boolean isAlive() throws RiakException
	{
		try
		{
			getConnection().ping();
			return true;
		}
		catch (RiakException e)
		{
			throw new RiakException("No response received.", e);
		}
	}
	
	@Override
	protected void doClose()
	{
		getConnection().shutdown();
	}
}