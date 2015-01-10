package org.skycrawl.nosqlshowcase.server.cassandra.view;

import org.skycrawl.nosqlshowcase.server.cassandra.CassandraConnection;
import org.skycrawl.nosqlshowcase.server.cassandra.controller.CassandraDataController;
import org.skycrawl.nosqlshowcase.server.root.ui.AbstractDatabaseUI;

import com.datastax.driver.core.Session;
import com.vaadin.annotations.Title;

@Title("Cassandra mini-app")
public class CassandraUI extends AbstractDatabaseUI<Session, CassandraDataController, CassandraConnection>
{
	private static final long	serialVersionUID	= 2343206316209127417L;
}