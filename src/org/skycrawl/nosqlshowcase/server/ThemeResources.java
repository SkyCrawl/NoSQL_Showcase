package org.skycrawl.nosqlshowcase.server;

public class ThemeResources
{
	/*
	 * GENERAL NOTES:
	 * - Resources can be both static (a downloadable WEB-INF file for instance) and dynamic (URL link needs to be constructed).
	 * - Stream resources allow creating dynamic resource content. Charts are typical examples of dynamic images.
	 * 
	 * NOTABLE SERVER-RELATED TYPES:
	 * - com.vaadin.server.FileDownloader
	 * - com.vaadin.server.ExternalResource
	 * - com.vaadin.server.StreamResource
	 * - Link - a link to internal or external resource - can open a new window
	 * 
	 * THEMERESOURCE NOTE:
	 * - {@link ThemeResource} (see below) looks for files in vaadin's static resource folder, see {@link #getVaadinRelativePathForResource(String)}.
	 */

	// ----------------------------------------------------------------
	// THEME RESOURCE DEFINITIONS
	// Suitable for creating resources, e.g.: "new ThemeResource(RELPATH_IMG_RIAK);"

	/*
	 * Database logos.
	 */
	public static final String			RELPATH_IMG_RIAK					= "images/riak_logo.png";
	public static final String			RELPATH_IMG_REDIS					= "images/redis_logo.png";
	public static final String			RELPATH_IMG_CASSANDRA				= "images/cassandra_logo.png";
	public static final String			RELPATH_IMG_MONGODB					= "images/mongoDB_logo.png";
	public static final String			RELPATH_IMG_NEO4J					= "images/neo4j_logo.png";

	/*
	 * Notification icons.
	 */
	public static final String			RELPATH_IMG_NOTIFICATIONS_INFO		= "images/Win8MetroIcons/notifications/icon-info-48x48.png";
	public static final String			RELPATH_IMG_NOTIFICATIONS_SUCCESS	= "images/Win8MetroIcons/notifications/icon-success-48x48.png";
	public static final String			RELPATH_IMG_NOTIFICATIONS_WARN		= "images/Win8MetroIcons/notifications/icon-warn-48x48.png";
	public static final String			RELPATH_IMG_NOTIFICATIONS_ERROR		= "images/Win8MetroIcons/notifications/icon-error-48x48.png";

	// ----------------------------------------------------------------
	// PUBLIC METHODS

	/**
	 * <p>
	 * Returns a real-time relative path of the desired resource that can be
	 * included statically in HTML. The resource is required to be in vaadin's
	 * static resource folder, e.g. "WebContent/VAADIN/themes/{theme-name}/".
	 * </p>
	 * 
	 * @param relativeResourcePath
	 *        theme-relative path to a file
	 */
	public static String getVaadinRelativePathForResource(String relativeResourcePath)
	{
		return "./VAADIN/themes/nosql_showcase/" + relativeResourcePath;
	}
}