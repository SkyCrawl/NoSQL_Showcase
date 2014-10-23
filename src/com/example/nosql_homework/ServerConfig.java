package com.example.nosql_homework;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.server.VaadinService;

public class ServerConfig
{
	// ----------------------------------------------------------------------------------
	// GENERAL CONFIGURATION
	
	/**
	 * Path to the application's "WebContent" directory.
	 */
	private static final String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	
	/**
	 * Path to the directory where image resources are stored.
	 */
	public static final String imagesPath = basepath + "/WEB-INF/images/";
	
	/**
	 * Information about which databases are supported. Maps database name to additional information and objects.
	 */
	public static final Map<String, DatabaseInfo> allDatabases = new HashMap<String, DatabaseInfo>(); // maps database name to additional information and objects
	
	// ----------------------------------------------------------------------------------
	// SPECIFIC/INSTANCE CONFIGURATION
	
	public static DatabaseInfo currentlySelectedDB = null;
}
