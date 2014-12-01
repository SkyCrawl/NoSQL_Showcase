package org.skycrawl.nosqlshowcase.server;

import java.util.logging.Level;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.skycrawl.nosqlshowcase.server.cassandra.CassandraInfo;
import org.skycrawl.nosqlshowcase.server.mongodb.MongoDBInfo;
import org.skycrawl.nosqlshowcase.server.neo4j.Neo4jInfo;
import org.skycrawl.nosqlshowcase.server.redis.RedisInfo;
import org.skycrawl.nosqlshowcase.server.riak.RiakInfo;

/**
 * Application life cycle event listener. Provides routines for startup/shutdown
 * of the web application.
 * 
 * @author SkyCrawl
 */
@WebListener
public class StartupAndQuitListener implements ServletContextListener
{
	/**
	 * Application startup event.
	 */
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		try
		{
			announceCheckOrAction("setting initial application state & essential variables");
			Config.setContext(event.getServletContext());
			// IOUtils.setAbsoluteBaseAppPath(event.getServletContext().getRealPath("/"));
			// VaadinService.getCurrent().getBaseDirectory().getAbsolutePath(); // path to the application's "WebContent" directory
			
			announceCheckOrAction("registering supported databases");
			Config.addSupportedDatabase(new RiakInfo());
			Config.addSupportedDatabase(new RedisInfo());
			Config.addSupportedDatabase(new CassandraInfo());
			Config.addSupportedDatabase(new MongoDBInfo());
			Config.addSupportedDatabase(new Neo4jInfo());

			Logger.log(Level.INFO, "\n**********************************************************\n"
					+ "APPLICATION SETUP SUCCESSFULLY FINISHED\n"
					+ "**********************************************************");
		}
		catch (Exception e)
		{
			Logger.log(Level.SEVERE, "APPLICATION LAUNCH REQUIREMENTS WERE NOT MET. ABORTED.");
			throw new IllegalStateException(e); // will be printed just afterwards
		}
	}

	/**
	 * Application shutdown event.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
	}
	
	// ---------------------------------------------------------------
	// PRIVATE INTERFACE

	private void announceCheckOrAction(String message)
	{
		Logger.log(Level.INFO, "**********************************************************\n" + "CHECKING REQUIREMENT / DOING ACTION: " + message);
	}
}