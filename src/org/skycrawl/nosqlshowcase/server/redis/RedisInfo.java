package org.skycrawl.nosqlshowcase.server.redis;

import java.util.Arrays;
import java.util.List;

import org.skycrawl.nosqlshowcase.server.ThemeResources;
import org.skycrawl.nosqlshowcase.server.root.db.IDatabaseInfo;
import org.skycrawl.nosqlshowcase.server.root.util.IVersionsSpecifier;

import com.vaadin.ui.UI;

public class RedisInfo implements IDatabaseInfo<RedisConnection>
{
	private static final long	serialVersionUID	= -6759838342083607954L;

	@Override
	public String getName()
	{
		return "Redis";
	}

	@Override
	public IVersionsSpecifier getSupportedVersions()
	{
		return new IVersionsSpecifier()
		{
			@Override
			public String getConciseString()
			{
				return "2.8.5";
			}
			
			@Override
			public List<String> getAllVersions()
			{
				return Arrays.asList("2.8.5");
			}
		};
	}

	@Override
	public String getSupportedClient()
	{
		return "Jedis v2.6.1";
	}
	
	@Override
	public int getDefaultPort()
	{
		// TODO:
		return 0;
	}
	
	@Override
	public String getBannerURL()
	{
		return ThemeResources.RELPATH_IMG_REDIS;
	}

	@Override
	public Class<? extends UI> getAssociatedUI()
	{
		return RedisUI.class;
	}

	@Override
	public Class<RedisConnection> getAssociatedConnection()
	{
		return RedisConnection.class;
	}
}