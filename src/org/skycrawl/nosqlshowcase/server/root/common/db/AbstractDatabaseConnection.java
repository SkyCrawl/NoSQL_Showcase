package org.skycrawl.nosqlshowcase.server.root.db;

import java.io.Serializable;

public abstract class AbstractDatabaseConnection<C extends Object, DC extends AbstractDataController<C>> implements Serializable
{
	private static final long	serialVersionUID	= 2253878732671568514L;
	
	private String hostname = null;
	private int port = -1;
	private C connection = null;
	private DC dataController;
	
	public void connect(String hostname, int port) throws Exception
	{
		if(isDefined())
		{
			throw new IllegalStateException("A connection is already established. Close it first.");
		}
		else
		{
			this.connection = doConnect(hostname, port);
			this.dataController = createDataController(this.connection);
			this.hostname = hostname;
			this.port = port;
		}
	}
	
	public void init() throws Exception
	{
		dataController.init();
	}
	
	public String getHostname()
	{
		return this.hostname;
	}

	public int getPort()
	{
		return this.port;
	}
	
	public boolean isHostnameDefined()
	{
		return hostname != null;
	}
	
	public boolean isPortDefined()
	{
		return port != -1;
	}

	public C getConnection()
	{
		return this.connection;
	}
	
	public DC getDataController()
	{
		return this.dataController;
	}

	public boolean isDefined() throws Exception
	{
		return (connection != null) && isAlive();
	}
	
	public void close()
	{
		doClose();
		connection = null;
	}
	
	public abstract String getDBVersion();
	
	protected abstract boolean isAlive() throws Exception;
	protected abstract C doConnect(String hostname, int port) throws Exception;
	protected abstract DC createDataController(C connection);
	protected abstract void doClose();
}