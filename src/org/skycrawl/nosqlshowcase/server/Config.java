package org.skycrawl.nosqlshowcase.server;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDatabaseConnection;
import org.skycrawl.nosqlshowcase.server.root.common.db.DatabaseHandle;
import org.skycrawl.nosqlshowcase.server.root.common.db.IDatabaseInfo;
import org.skycrawl.nosqlshowcase.server.root.ui.MasterUI;

import com.vaadin.ui.UI;

/**
 * <p>Java representation of some settings found it the "web.xml" file. These settings should not
 * relate to Vaadin or other technologies used (there are other ways to work with them) but rather
 * focus on settings that immediately affect the application's behaviour. At this time, only 
 * {@link #isCoreEnabled()} satisfies that condition.</p>
 * 
 * <p>Furthermore, this class defines some added value in the form of special interface not related
 * to the application's deployment descriptor that (again) affect its behaviour. These are mainly
 * "debug" and "devel features".</p>
 * 
 * @author SkyCrawl
 */
public class Config
{
	/*
	 * Inner variables.
	 */
	private static ServletContext WEB_APP_CONTEXT = null;
	private static Map<String, DatabaseHandle<?>> SERVLET_PATH_TO_DATABASE_INFO = new LinkedHashMap<String, DatabaseHandle<?>>();

	// ----------------------------------------------------------------------------------
	// PUBLIC INTERFACE

	/**
	 * Set the web application's context. All relevant settings found in the deployment
	 * descriptor will be derived from it.
	 * @see {@link #getContextParam(String)}
	 */
	public static void setContext(ServletContext context)
	{
		if (Config.WEB_APP_CONTEXT != null)
		{
			throw new IllegalStateException("Application context is already defined.");
		}
		else
		{
			Config.WEB_APP_CONTEXT = context;
		}
	}

	/**
	 * Get the web application's context.
	 */
	public static ServletContext getContext()
	{
		return WEB_APP_CONTEXT;
	}
	
	/**
	 * Registers support for the given database.
	 * 
	 * @param dbInfo
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws UnsupportedEncodingException 
	 */
	public static <CA extends AbstractDatabaseConnection<?,?>>  void addSupportedDatabase(IDatabaseInfo<CA> dbInfo) throws InstantiationException, IllegalAccessException
	{
		String servletPath = dbInfo.getName().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
		if(SERVLET_PATH_TO_DATABASE_INFO.containsKey(servletPath))
		{
			throw new IllegalStateException(String.format("Failed to associate servlet path '%s' with database '%s'. It was already associated with '%s'.",
					servletPath,
					dbInfo.getName(),
					SERVLET_PATH_TO_DATABASE_INFO.get(servletPath).getStaticInformation().getName()));
		}
		else
		{
			SERVLET_PATH_TO_DATABASE_INFO.put(servletPath, new DatabaseHandle<CA>(dbInfo));
		}
	}
	
	/**
	 * Gets the "servlet-url to database-info" mapping for supported databases.
	 * 
	 * @return
	 */
	public static Map<String, DatabaseHandle<?>> getSupportedDatabases()
	{
		return SERVLET_PATH_TO_DATABASE_INFO;
	}
	
	public static String getDefaultServletPath()
	{
		return "index";
	}
	
	public static Class<? extends UI> getDefaultUI()
	{
		return MasterUI.class;
	}
	
	// ----------------------------------------------------------------------------------
	// PRIVATE INTERFACE

	@SuppressWarnings({ "unchecked", "unused" })
	private static <T extends Object> T getContextParam(String key)
	{
		return (T) WEB_APP_CONTEXT.getInitParameter(key);
	}
}
