package org.skycrawl.nosqlshowcase.server.riak.model;

public class RiakWebsite extends AbstractRiakSingleLinkValue
{
	public String domain; // fully qualified
	
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