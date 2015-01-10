package org.skycrawl.nosqlshowcase.server.mongodb.view;

import org.skycrawl.nosqlshowcase.server.mongodb.MongoDBConnection;
import org.skycrawl.nosqlshowcase.server.mongodb.controller.MongoDBDataController;
import org.skycrawl.nosqlshowcase.server.root.ui.AbstractDatabaseUI;

import com.mongodb.DB;
import com.vaadin.annotations.Title;

@Title("MongoDB mini-app")
public class MongoDBUI extends AbstractDatabaseUI<DB, MongoDBDataController, MongoDBConnection>
{
	private static final long	serialVersionUID	= 2343206316209127417L;
}