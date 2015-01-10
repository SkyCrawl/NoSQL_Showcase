package org.skycrawl.nosqlshowcase.server.root.db;

public class DatabaseHandle<CA extends AbstractDatabaseConnection<?,?>>
{
	private final IDatabaseInfo<CA> staticInformation;
	private CA connectionHandle;
	
	public DatabaseHandle(IDatabaseInfo<CA> staticInformation) throws InstantiationException, IllegalAccessException
	{
		this.staticInformation = staticInformation;
		this.connectionHandle = staticInformation.getAssociatedConnection().newInstance(); 
	}

	public IDatabaseInfo<CA> getStaticInformation()
	{
		return staticInformation;
	}

	public CA getConnectionHandle()
	{
		return connectionHandle;
	}
	
	public boolean isConnectionDefined() throws Exception
	{
		return connectionHandle.isDefined();
	}
}