package org.skycrawl.nosqlshowcase.server.riak.controller;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.skycrawl.nosqlshowcase.server.Logger;
import org.skycrawl.nosqlshowcase.server.riak.model.AbstractRiakSingleLinkValue;
import org.skycrawl.nosqlshowcase.server.riak.model.RiakWebsite;
import org.skycrawl.nosqlshowcase.server.riak.model.RiakX509Cert;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDataController;
import org.skycrawl.nosqlshowcase.server.root.common.model.WebsiteToCertDataModel;
import org.skycrawl.nosqlshowcase.server.root.common.sample.DefaultCertObject;
import org.skycrawl.nosqlshowcase.server.root.ui.notifications.MyNotifications;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakLink;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.query.NodeStats;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

public class RiakDataController extends AbstractDataController<IRiakClient>
{
	/*
	 * All buckets this Riak mini-app uses.
	 */
	public static final String BUCKET_NAME_CERTIFICATES = "certificates";
	
	// private DomainBucket<RiakX509Cert> bucket_certs;
	private Bucket bucket_certs;
	
	public RiakDataController(IRiakClient connection)
	{
		super(connection);
	}
	
	@Override
	public void init() throws RiakException
	{
		// this.bucket_certs = DomainBucket.builder(getBucket("certificates"), RiakX509Cert.class).build();
		this.bucket_certs = getBucket(BUCKET_NAME_CERTIFICATES);
	}
	
	public Iterable<NodeStats> getNodeStats() throws RiakException
	{
		return getConnection().stats();
	}

	// ----------------------------------------------------------------
	// INDIVIDUAL DATA MANIPULATION ROUTINES

	@SuppressWarnings("deprecation")
	public Set<String> getAllBucketNames() throws RiakException
	{
		try
		{
			return getConnection().listBuckets();
		}
		catch (Exception e)
		{
			Logger.logThrowable("Could not fetch buckets: ", e);
			throw new RiakException(e);
		}
	}
	
	/**
	 * Gets the given bucket. If it does not exist yet, it is created.
	 * 
	 * @param name
	 * @return
	 * @throws RiakException
	 */
	public Bucket getBucket(String name) throws RiakException
	{
		return getConnection().fetchBucket(name).execute();
	}
	
	public RiakX509Cert linkWalkToRoot(RiakWebsite start) throws RiakException
	{
		AbstractRiakSingleLinkValue current = start;
		while(current.toLink() != null)
		{
			RiakLink next = current.toLink();
			if(!next.getBucket().equals(BUCKET_NAME_CERTIFICATES))
			{
				throw new RiakException(String.format("Invalid link binding. Item with key '%s' contains a link to bucket '%s'. Expected: '%s'.",
						current.toKey(), next.getBucket(), BUCKET_NAME_CERTIFICATES)); 
			}
			else
			{
				current = getBucket(next.getBucket()).fetch(next.getKey(), RiakX509Cert.class).execute();
			}
		}
		return (RiakX509Cert) current;
	}
	
	@Override
	public Set<String> getTLDs() throws RiakException
	{
		Set<String> tlds = getAllBucketNames();
		tlds.remove(RiakDataController.BUCKET_NAME_CERTIFICATES);
		return tlds;
	}

	@Override
	public WebsiteToCertDataModel getSetDomainAndIntersections(Set<String> tlds) throws RiakException
	{
		WebsiteToCertDataModel result = new WebsiteToCertDataModel();
		try
		{
			// and turn them into Venn data
			for(String tld : tlds)
			{
				result.registerSet(tld);
				
				Bucket bucket = getBucket(tld);
				for(String domain : bucket.keys())
				{
					// fetch the website database entity
					RiakWebsite website = bucket.fetch(domain, RiakWebsite.class).execute();
					
					// determine root CA for it
					RiakX509Cert rootCA = linkWalkToRoot(website);
					
					// register the certificate
					result.registerSet(rootCA);
					
					// and register the current found overlap
					result.registerOverlap(domain, tld, rootCA);
				}
			}
			return result;
		}
		catch (Exception e)
		{
			Logger.logThrowable("Could not create mini-app: ", e);
			MyNotifications.showError("Mini app could not be launched", e.getLocalizedMessage(), null);
			return null;
		}
	}
	
	/**
	 * @param fqn fully qualified domain name to which the certificate chain belongs
	 * @param certificate_chain
	 * @return
	 */
	@Override
	public boolean store(URL url, List<DefaultCertObject> certificateChain)
	{
		/*
		 * Root authorities need to be first so that we can store them and
		 * create links from subsequent authorities in linear fashion.
		 */
		Collections.reverse(certificateChain);
		
		try
		{
			// prepare to store and link certificates
			PeekingIterator<DefaultCertObject> it = Iterators.peekingIterator(certificateChain.iterator());
			RiakX509Cert current = null; // remember the website's certificate when iterating finishes
			
			// first store the root CA
			current = new RiakX509Cert(it.next());
			bucket_certs.store(current.toKey(), current).execute();
			
			// then continue storing certificate chain and link certificates
			while(it.hasNext())
			{
				// do store
				RiakX509Cert next = new RiakX509Cert(it.next());
				next.useLink(new RiakLink(BUCKET_NAME_CERTIFICATES, current.toKey(), ""));
				bucket_certs.store(next.toKey(), next).execute();
				
				// prepare for next iteration
				current = next;
			}
			
			// System.out.println(url.getAuthority());
			// System.out.println(url.getHost());
			// System.out.println(url.getPath());
			
			// store and link website
			RiakWebsite website = new RiakWebsite(url.getHost());
			website.useLink(new RiakLink(BUCKET_NAME_CERTIFICATES, current.toKey(), ""));
			getBucket(StringUtils.substringAfterLast(website.toKey(), ".")).store(website.toKey(), website).execute(); // each website is stored into a bucket of a TLD!
			
			return true;
		}
		catch (RiakException e)
		{
			Logger.logThrowable("Not supposed to happen: ", e);
			return false;
		}
	}
	
	// ----------------------------------------------------------------
	// INHERITED MASSIVE MANIPULATION ROUTINES

	@Override
	public void clearDatabase() throws RiakException
	{
		for(String bucketName : getAllBucketNames())
		{
			Bucket bucket = getBucket(bucketName);
			for(String key : bucket.keys())
			{
				bucket.delete(key).execute();
			}
		}
	}
}