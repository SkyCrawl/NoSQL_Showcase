package org.skycrawl.nosqlshowcase.server.cassandra;

import java.util.Arrays;
import java.util.List;

import org.skycrawl.nosqlshowcase.server.ThemeResources;
import org.skycrawl.nosqlshowcase.server.cassandra.view.CassandraUI;
import org.skycrawl.nosqlshowcase.server.root.common.db.IDatabaseInfo;
import org.skycrawl.nosqlshowcase.server.root.common.db.IVersionsSpecifier;

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
				return "1.2, 2.0, 2.1";
			}
			
			@Override
			public List<String> getAllVersions()
			{
				return Arrays.asList("1.2", "2.0", "2.1");
			}
		};
	}

	@Override
	public String getSupportedClient()
	{
		return "DataStax Java Driver for Apache Cassandra v2.1.3";
	}
	
	@Override
	public int getDefaultPort()
	{
		return 9042;
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