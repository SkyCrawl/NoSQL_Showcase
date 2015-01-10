package org.skycrawl.nosqlshowcase.server.riak.view;

import org.skycrawl.nosqlshowcase.server.riak.RiakConnection;
import org.skycrawl.nosqlshowcase.server.riak.controller.RiakDataController;
import org.skycrawl.nosqlshowcase.server.root.ui.AbstractDatabaseUI;

import com.basho.riak.client.IRiakClient;
import com.vaadin.annotations.Title;

@Title("Riak mini-app")
public class RiakUI extends AbstractDatabaseUI<IRiakClient, RiakDataController, RiakConnection>
{
	private static final long	serialVersionUID	= 8667268709910619633L;
}