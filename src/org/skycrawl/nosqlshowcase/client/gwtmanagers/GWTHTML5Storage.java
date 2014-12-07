package org.skycrawl.nosqlshowcase.client.gwtmanagers;

import com.google.gwt.storage.client.Storage;

/**
 * Manages client's HTML5 storage and provides some routines over it. HTML5 client storage
 * is a way to store various data on the client, offline. It can be used to, for instance,
 * cache some large data and speed up page loading. Properties:
 * <ul>
 * <li> Maximum amount of data is 5MB.
 * <li> The data is saved into browser cache which is NOT secure. Avoid storing any
 * <li> Requires GWT >= 2.3.
 * sensitive information.
 * </ul>
 * 
 * Comparison with cookies:
 * <ul>
 * <li> Cookies have a global scope - they are accessible by all browser windows and tabs.
 * <li> Cookies have a limited size of 4KB, up to 20 maximum cookies per domain.
 * <li> Cookies are sent with every request automatically which slows down both client and
 * server.
 * <li> HTML5 storage provides both "session" (single window or tab) and "local" (cross window
 * and tab) scope. Both scopes can only store strings in key/value pairs.
 * <li> HTML5 session storage doesn't have a size limit. Local storage up to 5MB per domain.
 * </ul>
 * 
 * <p>HTML5 specification also defines storage events for subscribers and although it is already
 * implemented by GWT, it is not yet implemented by most browsers.</p>
 * 
 * @author SkyCrawl
 */
public class GWTHTML5Storage
{
	public static boolean isSupported()
	{
		return isLocalStorageSupported() && isSessionStorageSupported();
	}
	
	public static boolean isSessionStorageSupported()
	{
		return Storage.getSessionStorageIfSupported() != null;
	}
	
	public static boolean isLocalStorageSupported()
	{
		return Storage.getLocalStorageIfSupported() != null;
	}
	
	public static Storage getLocalStorage()
	{
		return Storage.getLocalStorageIfSupported();
	}
	
	public static Storage getSessionStorage()
	{
		return Storage.getSessionStorageIfSupported();
	}
}