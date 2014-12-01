package org.skycrawl.nosqlshowcase.server.riak;

import java.util.ArrayList;
import java.util.List;

import org.skycrawl.nosqlshowcase.server.ThemeResources;
import org.skycrawl.nosqlshowcase.server.riak.view.RiakUI;
import org.skycrawl.nosqlshowcase.server.root.db.IDatabaseInfo;
import org.skycrawl.nosqlshowcase.server.root.util.IVersionsSpecifier;

import com.basho.riak.client.raw.pbc.PBClientConfig;
import com.vaadin.ui.UI;

public class RiakInfo implements IDatabaseInfo<RiakConnection>
{
	private static final long	serialVersionUID	= -7653190686032177845L;

	@Override
	public String getName()
	{
		return "Riak";
	}

	@Override
	public IVersionsSpecifier getSupportedVersions()
	{
		return new IVersionsSpecifier()
		{
			@Override
			public String getConciseString()
			{
				return "1.4.4 - 1.4.10";
			}
			
			@Override
			public List<String> getAllVersions()
			{
				List<String> result = new ArrayList<String>();
				for(int i = 4; i <= 10; i++)
				{
					result.add(String.format("1.4.%d", i));
				}
				return result;
			}
		};
	}

	@Override
	public String getSupportedClient()
	{
		return "Basho Java client v1.4.4";
	}
	
	@Override
	public int getDefaultPort()
	{
		return new PBClientConfig.Builder().build().getPort();
	}
	
	@Override
	public String getBannerURL()
	{
		return ThemeResources.RELPATH_IMG_RIAK;
	}

	@Override
	public Class<? extends UI> getAssociatedUI()
	{
		return RiakUI.class;
	}

	@Override
	public Class<RiakConnection> getAssociatedConnection()
	{
		return RiakConnection.class;
	}
}