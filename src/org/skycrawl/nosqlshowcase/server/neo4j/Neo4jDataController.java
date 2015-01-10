package org.skycrawl.nosqlshowcase.server.neo4j;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDataController;
import org.skycrawl.nosqlshowcase.server.root.common.model.WebsiteToCertDataModel;
import org.skycrawl.nosqlshowcase.server.root.common.sample.DefaultCertObject;

public class Neo4jDataController extends AbstractDataController<Object>
{
	public Neo4jDataController(Object connection)
	{
		super(connection);
	}

	@Override
	public void init() throws Exception
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public Set<String> getTLDs() throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebsiteToCertDataModel getSetDomainAndIntersections(Set<String> tlds) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean store(URL website, List<DefaultCertObject> certificateChain) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	//----------------------------------------------------------------
	// INHERITED MASSIVE MANIPULATION ROUTINES
	
	@Override
	public void clearDatabase()
	{
		// TODO Auto-generated method stub
	}
}