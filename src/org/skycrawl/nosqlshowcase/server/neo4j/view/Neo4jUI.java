package org.skycrawl.nosqlshowcase.server.neo4j.view;

import org.skycrawl.nosqlshowcase.server.neo4j.Neo4jConnection;
import org.skycrawl.nosqlshowcase.server.neo4j.Neo4jQueryWrapper;
import org.skycrawl.nosqlshowcase.server.neo4j.controller.Neo4jDataController;
import org.skycrawl.nosqlshowcase.server.root.ui.AbstractDatabaseUI;

import com.vaadin.annotations.Title;

@Title("Neo4j mini-app")
public class Neo4jUI extends AbstractDatabaseUI<Neo4jQueryWrapper, Neo4jDataController, Neo4jConnection>
{
	private static final long	serialVersionUID	= 2343206316209127417L;
}