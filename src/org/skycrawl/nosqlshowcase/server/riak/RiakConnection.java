package org.skycrawl.nosqlshowcase.server.riak;

import org.skycrawl.nosqlshowcase.server.riak.controller.RiakDataController;
import org.skycrawl.nosqlshowcase.server.root.db.AbstractDatabaseConnection;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;

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
		/*
		 * NORMALLY, the following could be used. It is not supported by protobuff connections, however.
		 * 
		try
		{
			for(NodeStats stats : getConnection().stats())
			{
				if(stats.riakCoreVersion() != null)
				{
					return stats.riakCoreVersion();
				}
			}
		}
		catch (RiakException e)
		{
			Logger.logThrowable("Could not fetch Riak version. Returning null...", e);
		}
		*/
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