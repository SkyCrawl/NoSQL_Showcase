package org.skycrawl.nosqlshowcase.server.riak.controller;

import java.util.ArrayList;
import java.util.List;

public class RiakSampleLoadResult
{
	private final List<String> malformedURLs;
	private final List<String> urlsWithInvalidResponse;
	private final List<String> ignoredURLs;
	private final List<String> failedToSaveURLs;
	
	public RiakSampleLoadResult()
	{
		this.malformedURLs = new ArrayList<String>();
		this.urlsWithInvalidResponse = new ArrayList<String>();
		this.ignoredURLs = new ArrayList<String>();
		this.failedToSaveURLs = new ArrayList<String>(); 
	}

	public List<String> getMalformedURLs()
	{
		return this.malformedURLs;
	}

	public List<String> getUrlsWithInvalidResponse()
	{
		return this.urlsWithInvalidResponse;
	}

	public List<String> getIgnoredURLs()
	{
		return this.ignoredURLs;
	}
	
	public boolean loadWasACompleteSuccess()
	{
		return malformedURLs.isEmpty() && urlsWithInvalidResponse.isEmpty() && ignoredURLs.isEmpty();
	}

	public List<String> getFailedToSaveURLs()
	{
		return this.failedToSaveURLs;
	}
}