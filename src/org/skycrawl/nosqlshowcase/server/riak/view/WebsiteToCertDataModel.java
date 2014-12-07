package org.skycrawl.nosqlshowcase.server.riak.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.skycrawl.nosqlshowcase.server.riak.model.RiakX509Cert;
import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennOverlap;
import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennSet;

public class WebsiteToCertDataModel
{
	/**
	 * Key to venn set mapping. Sets represent TLDs and {@link RiakX509Cert root CAs}.
	 * Keys are taken from database.
	 */
	private final Map<String, VennSet> sets;
	
	/**
	 * Venn overlaps, always between two sets - one TLD and one {@link RiakX509Cert CA}.
	 */
	private final Map<TLDtoCertMapping, List<String>> overlaps;
	
	public WebsiteToCertDataModel()
	{
		this.sets = new HashMap<String, VennSet>();
		this.overlaps = new HashMap<TLDtoCertMapping, List<String>>();
	}
	
	public void registerSet(String tld)
	{
		registerSet(tld, new VennSet(tld.toUpperCase(), 0));
	}
	
	public void registerSet(RiakX509Cert cert)
	{
		String setLabel = cert.getOrganizationName();
		if((setLabel == null) || setLabel.isEmpty())
		{
			setLabel = cert.getOrganizationUnit(); 
		}
		if((setLabel == null) || setLabel.isEmpty())
		{
			setLabel = cert.getCommonName(); 
		}
		if((setLabel == null) || setLabel.isEmpty())
		{
			throw new IllegalArgumentException("Could not determine venn set label from the given certificate."); 
		}
		registerSet(cert.toKey(), new VennSet(setLabel, 0));
	}
	
	/**
	 * This is not checked in underlying code but each website must only be registered ONCE!
	 * 
	 * @param website
	 * @param tld
	 * @param cert
	 */
	public void registerOverlap(String website, String tld, RiakX509Cert cert)
	{
		VennSet tldSet = sets.get(tld);
		VennSet certSet = sets.get(cert.toKey());
		
		// first check whether the given arguments are mapped to sets
		if(tldSet == null)
		{
			throw new IllegalStateException("The given TLD has not been registered as a set.");
		}
		if(certSet == null)
		{
			throw new IllegalStateException("The given certificate has not been registered as a set.");
		}
		
		// register website in the mapped sets
		tldSet.setSize(tldSet.getSize() + 1);
		certSet.setSize(certSet.getSize() + 1);
		
		// and then the actual registration
		TLDtoCertMapping overlapKey = new TLDtoCertMapping(tldSet, certSet); // IMPORTANT: this order is used below
		if(!overlaps.containsKey(overlapKey))
		{
			overlaps.put(overlapKey, new ArrayList<String>());
		}
		overlaps.get(overlapKey).add(website);
	}
	
	public void fillDataForTLD(String tld, List<VennSet> sets, List<VennOverlap> overlaps)
	{
		// first some checks
		VennSet tldSet = this.sets.get(tld);
		if(tldSet == null)
		{
			throw new IllegalStateException("The given TLD has not been registered as a set.");
		}
		
		// and then some logic
		sets.add(tldSet);
		for(Entry<TLDtoCertMapping, List<String>> entry : this.overlaps.entrySet())
		{
			// IMPORTANT: this order is used above
			if(entry.getKey().getValue1().equals(tldSet))
			{
				// also add the root CA set
				sets.add(entry.getKey().getValue2()); // it is required to be unique, otherwise the instances of {@link TLDtoCertMapping} would be the same
				overlaps.add(new VennOverlap(
						new int[] { 0, sets.size() - 1 },
						entry.getValue().size()
				));
			}
		}
	}
	
	public void clear()
	{
		sets.clear();
		overlaps.clear();
	}
	
	//---------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void registerSet(String key, VennSet set)
	{
		if(!sets.containsKey(key))
		{
			sets.put(key, set);
		}
	}
}