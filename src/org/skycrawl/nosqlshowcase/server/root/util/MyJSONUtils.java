package org.skycrawl.nosqlshowcase.server.root.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.skycrawl.nosqlshowcase.server.Logger;

public class MyJSONUtils
{
	private static final ObjectMapper	mapper	= new ObjectMapper();

	public static String toJson(Object object)
	{
		try
		{
			return mapper.writeValueAsString(object);
		}
		catch (Exception e)
		{
			Logger.logThrowable("Could not serialize object to JSON: ", e);
			return null;
		}
	}
}