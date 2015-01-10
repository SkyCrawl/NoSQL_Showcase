package org.skycrawl.nosqlshowcase.server.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.skycrawl.nosqlshowcase.server.ThemeResources;
import org.skycrawl.nosqlshowcase.server.mongodb.view.MongoDBUI;
import org.skycrawl.nosqlshowcase.server.root.common.db.IDatabaseInfo;
import org.skycrawl.nosqlshowcase.server.root.common.db.IVersionsSpecifier;

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
				return "2.0 - 2.8";
			}
			
			@Override
			public List<String> getAllVersions()
			{
				List<String> result = new ArrayList<String>();
				for(int i = 0; i <= 8; i++)
				{
					result.add(String.format("2.%d", i));
				}
				return result;
			}
		};
	}

	@Override
	public String getSupportedClient()
	{
		return "mongodb-java-driver v2.14.4";
	}
	
	@Override
	public int getDefaultPort()
	{
		return 27017;
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