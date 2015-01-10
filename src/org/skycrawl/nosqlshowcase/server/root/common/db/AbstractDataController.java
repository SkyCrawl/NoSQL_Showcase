package org.skycrawl.nosqlshowcase.server.root.common.db;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.skycrawl.nosqlshowcase.server.root.common.exceptions.DuplicateItemException;
import org.skycrawl.nosqlshowcase.server.root.common.model.WebsiteToCertDataModel;
import org.skycrawl.nosqlshowcase.server.root.common.sample.DefaultCertObject;

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
	public abstract Set<String> getTLDs() throws Exception;;
	public abstract WebsiteToCertDataModel getSetDomainAndIntersections(Set<String> tlds) throws Exception;
	public abstract boolean store(URL website, List<DefaultCertObject> certificateChain) throws DuplicateItemException, Exception;
	public abstract void clearDatabase() throws Exception;
}