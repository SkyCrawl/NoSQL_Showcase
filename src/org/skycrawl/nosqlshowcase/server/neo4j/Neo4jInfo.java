package org.skycrawl.nosqlshowcase.server.neo4j;

import java.util.Arrays;
import java.util.List;

import org.skycrawl.nosqlshowcase.server.ThemeResources;
import org.skycrawl.nosqlshowcase.server.neo4j.view.Neo4jUI;
import org.skycrawl.nosqlshowcase.server.root.common.db.IDatabaseInfo;
import org.skycrawl.nosqlshowcase.server.root.common.db.IVersionsSpecifier;

import com.vaadin.ui.UI;

public class Neo4jInfo implements IDatabaseInfo<Neo4jConnection>
{
	private static final long	serialVersionUID	= -196012471122472824L;

	@Override
	public String getName()
	{
		return "Neo4j";
	}

	@Override
	public IVersionsSpecifier getSupportedVersions()
	{
		return new IVersionsSpecifier()
		{
			@Override
			public String getConciseString()
			{
				return "2.1.6";
			}
			
			@Override
			public List<String> getAllVersions()
			{
				return Arrays.asList("2.1.6");
			}
		};
	}

	@Override
	public String getSupportedClient()
	{
		return "Neo4j Java Client v2.1.6";
	}
	
	@Override
	public int getDefaultPort()
	{
		return 7474;
	}
	
	@Override
	public String getBannerURL()
	{
		return ThemeResources.RELPATH_IMG_NEO4J;
	}

	@Override
	public Class<? extends UI> getAssociatedUI()
	{
		return Neo4jUI.class;
	}

	@Override
	public Class<Neo4jConnection> getAssociatedConnection()
	{
		return Neo4jConnection.class;
	}
}