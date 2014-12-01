package org.skycrawl.nosqlshowcase.server.mongodb;

import java.util.Arrays;
import java.util.List;

import org.skycrawl.nosqlshowcase.server.ThemeResources;
import org.skycrawl.nosqlshowcase.server.root.db.IDatabaseInfo;
import org.skycrawl.nosqlshowcase.server.root.util.IVersionsSpecifier;

import com.vaadin.ui.UI;

public class MongoDBInfo implements IDatabaseInfo<MongoDBConnection>
{
	private static final long	serialVersionUID	= 8695456331453288656L;

	@Override
	public String getName()
	{
		return "MongoDB";
	}

	@Override
	public IVersionsSpecifier getSupportedVersions()
	{
		return new IVersionsSpecifier()
		{
			@Override
			public String getConciseString()
			{
				return "TODO";
			}
			
			@Override
			public List<String> getAllVersions()
			{
				return Arrays.asList("TODO");
			}
		};
	}

	@Override
	public String getSupportedClient()
	{
		return "TODO";
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
		return ThemeResources.RELPATH_IMG_MONGODB;
	}

	@Override
	public Class<? extends UI> getAssociatedUI()
	{
		return MongoDBUI.class;
	}

	@Override
	public Class<MongoDBConnection> getAssociatedConnection()
	{
		return MongoDBConnection.class;
	}
}