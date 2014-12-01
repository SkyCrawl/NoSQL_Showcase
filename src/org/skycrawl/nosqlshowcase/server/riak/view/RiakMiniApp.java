package org.skycrawl.nosqlshowcase.server.riak.view;

import org.skycrawl.nosqlshowcase.server.riak.controller.RiakDataController;
import org.skycrawl.nosqlshowcase.server.root.ui.util.IMiniApp;

import com.basho.riak.client.RiakException;
import com.vaadin.ui.Panel;

public class RiakMiniApp extends Panel implements IMiniApp<RiakDataController>
{
	private static final long	serialVersionUID	= -7747811508882278766L;

	@Override
	public void refresh(RiakDataController dataController)
	{
		// TODO
		
		try
		{
			for(String name : dataController.getAllBucketNames())
			{
				System.out.println(name);
			}
		}
		catch (RiakException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}