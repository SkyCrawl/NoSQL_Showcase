package org.skycrawl.nosqlshowcase.server.riak.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.skycrawl.nosqlshowcase.server.Logger;
import org.skycrawl.nosqlshowcase.server.ServletResources;
import org.skycrawl.nosqlshowcase.server.riak.model.RiakWebsite;
import org.skycrawl.nosqlshowcase.server.riak.model.RiakX509Cert;
import org.skycrawl.nosqlshowcase.server.root.db.AbstractDataController;
import org.skycrawl.nosqlshowcase.server.root.ui.dialogs.GeneralDialogs;
import org.skycrawl.nosqlshowcase.server.root.ui.notifications.MyNotifications;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakLink;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.query.NodeStats;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;

import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;

public class RiakDataController extends AbstractDataController<IRiakClient>
{
	/*
	 * All buckets this Riak mini-app uses.
	 */
	public static final String BUCKET_NAME_CERTIFICATES = "certificates";
	public static final String BUCKET_NAME_WEBSITES = "websites"; // TODO: use it only if we can't list bucket names
	
	// private DomainBucket<RiakX509Cert> bucket_certs;
	private Bucket bucket_certs;
	private Bucket bucket_websites;
	
	public RiakDataController(IRiakClient connection)
	{
		super(connection);
	}
	
	@Override
	public void init() throws RiakException
	{
		// this.bucket_certs = DomainBucket.builder(getBucket("certificates"), RiakX509Cert.class).build();
		this.bucket_certs = getBucket(BUCKET_NAME_CERTIFICATES);
		this.bucket_websites = getBucket(BUCKET_NAME_WEBSITES);
	}
	
	public Iterable<NodeStats> getNodeStats() throws RiakException
	{
		return getConnection().stats();
	}

	// ----------------------------------------------------------------
	// INDIVIDUAL DATA MANIPULATION ROUTINES

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
	
	/**
	 * @param fqn fully qualified domain name to which the certificate chain belongs
	 * @param certificate_chain
	 * @return
	 */
	public boolean store(URL url, List<RiakX509Cert> certificate_chain)
	{
		/*
		 * Root authorities need to be first so that we can store them and
		 * create links from subsequent authorities in linear fashion.
		 */
		Collections.reverse(certificate_chain);
		
		try
		{
			// store and link certificates
			PeekingIterator<RiakX509Cert> it = Iterators.peekingIterator(certificate_chain.iterator());
			RiakX509Cert current = null, next; // remember the website's certificate when iterating finishes
			while(it.hasNext())
			{
				current = it.next();
				next = it.hasNext() ? it.peek() : null;
				
				bucket_certs.store(current.toKey(), current).execute();
				if(next != null)
				{
					next.useLink(new RiakLink(BUCKET_NAME_CERTIFICATES, current.toKey(), ""));
					bucket_certs.store(next.toKey(), next).execute();
				}
			}
			
			System.out.println(url.getAuthority());
			System.out.println(url.getHost());
			System.out.println(url.getPath());
			
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

	@Override
	public void loadSampleData() throws IOException
	{
		final RiakSampleLoader websiteLoader = new RiakSampleLoader(this); 
		try (BufferedReader br = new BufferedReader(new InputStreamReader(ServletResources.getResourceAsStream(ServletResources.SAMPLE_DATA_RIAK))))
		{
			String url;
			while ((url = br.readLine()) != null)
			{
				websiteLoader.load(url);
			}
		}
		finally
		{
			if(websiteLoader.getLoadResult().loadWasACompleteSuccess())
			{
				MyNotifications.showSuccess(null, "Sample data successfully loaded.", null);
			}
			else
			{
				MyNotifications.showWarning("Sample data load result", "Click to display details...", new ClickListener()
				{
					private static final long	serialVersionUID	= 8456228417991600455L;

					@Override
					public void click(ClickEvent event)
					{
						TabSheet report = new TabSheet();
						report.setSizeFull();
						if(!websiteLoader.getLoadResult().getMalformedURLs().isEmpty())
						{
							report.addTab(getComponentFor(websiteLoader.getLoadResult().getMalformedURLs()), "Malformed URLs");
						}
						if(!websiteLoader.getLoadResult().getUrlsWithInvalidResponse().isEmpty())
						{
							report.addTab(getComponentFor(websiteLoader.getLoadResult().getUrlsWithInvalidResponse()), "Invalid response URLs");
						}
						if(!websiteLoader.getLoadResult().getFailedToSaveURLs().isEmpty())
						{
							report.addTab(getComponentFor(websiteLoader.getLoadResult().getFailedToSaveURLs()), "Not saved URLs");
						}
						if(!websiteLoader.getLoadResult().getIgnoredURLs().isEmpty())
						{
							report.addTab(getComponentFor(websiteLoader.getLoadResult().getIgnoredURLs()), "Ignored URLs");
						}
						
						MessageBox mb = GeneralDialogs.componentDialog("Sample data load report", Icon.WARN, report);
						mb.setWidth("600px");
						mb.setHeight("400px");
					}
					
					private Component getComponentFor(List<String> urlList)
					{
						TextArea result = new TextArea(null, StringUtils.join(urlList.iterator(), '\n'));
						result.setSizeFull();
						result.setWordwrap(false);
						return result;
					}
				});
			}
		}
	}
}