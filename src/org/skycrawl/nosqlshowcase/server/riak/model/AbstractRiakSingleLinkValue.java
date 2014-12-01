package org.skycrawl.nosqlshowcase.server.riak.model;

import java.util.ArrayList;
import java.util.Collection;

import com.basho.riak.client.RiakLink;
import com.basho.riak.client.convert.RiakLinks;

public abstract class AbstractRiakSingleLinkValue
{
	@RiakLinks
	public Collection<RiakLink> links;

	public AbstractRiakSingleLinkValue()
	{
		this.links = new ArrayList<RiakLink>();
	}
	
	public abstract String toKey();
	
	public void useLink(RiakLink link)
	{
		this.links.clear();
		this.links.add(link);
	}
	
	public RiakLink toLink()
	{
		return this.links.isEmpty() ? null : this.links.iterator().next();
	}
}