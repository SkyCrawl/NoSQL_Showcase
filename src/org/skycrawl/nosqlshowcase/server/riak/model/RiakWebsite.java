package org.skycrawl.nosqlshowcase.server.riak.model;

public class RiakWebsite extends AbstractRiakSingleLinkValue
{
	public String domain; // fully qualified
	
	/**
	 * Default constructor makes Jackson happy.
	 */
	protected RiakWebsite()
	{
	}
	
	public RiakWebsite(String domain)
	{
		this.domain = domain;
	}

	@Override
	public String toKey()
	{
		return this.domain;
	}

	public String getDomain()
	{
		return this.domain;
	}
}