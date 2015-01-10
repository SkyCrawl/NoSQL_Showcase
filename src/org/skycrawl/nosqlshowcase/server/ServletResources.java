package org.skycrawl.nosqlshowcase.server;

import java.io.InputStream;

public class ServletResources
{
	public static final String SAMPLE_DATA = "/WEB-INF/samples/riak.txt";
	
	public static InputStream getResourceAsStream(String path)
	{
		return Config.getContext().getResourceAsStream(path);
	}
}