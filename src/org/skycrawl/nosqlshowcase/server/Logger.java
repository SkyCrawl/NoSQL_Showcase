package org.skycrawl.nosqlshowcase.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

/**
 * Special logger wrapper to be used by the web application.
 * 
 * @author SkyCrawl
 */
public class Logger
{
	private static final java.util.logging.Logger innerLogger = java.util.logging.Logger.getAnonymousLogger();

	public static void log(Level logLevel, String message)
	{
		innerLogger.log(logLevel, message);
	}

	public static void logThrowable(String message, Throwable t)
	{
		innerLogger.log(Level.SEVERE, "exception occured: " + message + "\n" + throwableToStackTrace(t));
	}

	private static String throwableToStackTrace(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		Throwable tt = t;
		while (tt != null)
		{
			tt.printStackTrace(pw); // not an error but a feature
			tt = tt.getCause();
			if (tt != null)
			{
				pw.print("caused by: ");
			}
		}
		return sw.toString();
	}
}