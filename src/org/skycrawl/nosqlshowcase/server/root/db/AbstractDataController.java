package org.skycrawl.nosqlshowcase.server.root.db;

public abstract class AbstractDataController<C extends Object>
{
	private C connection;
	
	public AbstractDataController(C connection)
	{
		this.connection = connection;
	}
	
	protected C getConnection()
	{
		return this.connection;
	}
	
	public abstract void init() throws Exception;
	public abstract void clearDatabase() throws Exception;
	public abstract void loadSampleData() throws Exception;
}