package org.skycrawl.nosqlshowcase.server.cassandra;

import java.util.Arrays;
import java.util.List;

import org.skycrawl.nosqlshowcase.server.ThemeResources;
import org.skycrawl.nosqlshowcase.server.root.db.IDatabaseInfo;
import org.skycrawl.nosqlshowcase.server.root.util.IVersionsSpecifier;

import com.vaadin.ui.UI;

public class CassandraInfo implements IDatabaseInfo<CassandraConnection>
{
	private static final long	serialVersionUID	= -2474008112047080010L;

	@Override
	public String getName()
	{
		return "Cassandra";
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
		return ThemeResources.RELPATH_IMG_CASSANDRA;
	}

	@Override
	public Class<? extends UI> getAssociatedUI()
	{
		return CassandraUI.class;
	}

	@Override
	public Class<CassandraConnection> getAssociatedConnection()
	{
		return CassandraConnection.class;
	}
}