package org.skycrawl.nosqlshowcase.server.root.filters;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.skycrawl.nosqlshowcase.server.Config;
import org.skycrawl.nosqlshowcase.server.Logger;
import org.skycrawl.nosqlshowcase.server.root.requests.HttpRequestUtils;

/**
 * Base class for all {@link Filter filters} of this application. Provides
 * special filter-related optional interface.
 * 
 * @author SkyCrawl
 */
public abstract class AbstractFilter implements Filter
{
	// ----------------------------------------------------------
	// SINGLE (AND SIMPLE) IMPLEMENTATION OF SOME INHERITED ROUTINES

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
	}

	@Override
	public void destroy()
	{
	}

	// ----------------------------------------------------------
	// REQUEST DISPATCHING ROUTINES

	/**
	 * Client-side redirect. Affects the client browser's address bar.
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void clientRedirect(ServletResponse servletResponse, String redirectString) throws IOException, ServletException
	{
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		httpResponse.sendRedirect(redirectString.startsWith("/") ? redirectString.substring(1) : redirectString);
	}

	/**
	 * @Deprecated: debug first - see
	 *              {@link #clientRedirect(ServletResponse, String)} Server-side
	 *              forward. Doesn't affect the client browser's address bar.
	 * @throws IOException
	 * @throws ServletException
	 */
	@Deprecated
	protected void serverForward(ServletRequest servletRequest, ServletResponse servletResponse, String forwardString) throws IOException, ServletException
	{
		servletRequest.getRequestDispatcher(forwardString).forward(servletRequest, servletResponse);
	}

	// ----------------------------------------------------------
	// PROGRAMMATIC HELPER ROUTINES

	protected boolean isServletPathDefined(HttpServletRequest httpRequest)
	{
		return HttpRequestUtils.getServletPathWhetherMappedOrNot(httpRequest) != null;
	}

	// ----------------------------------------------------------
	// DEBUG ROUTINES, ALTER TO FIT YOUR NEEDS

	protected boolean isApplicationInProductionMode()
	{
		return Boolean.parseBoolean(Config.getContext().getInitParameter("productionMode"));
	}

	protected boolean shouldPrintDebugInfo()
	{
		return Boolean.parseBoolean(Config.getContext().getInitParameter("printDebugInfoInFilters"));
	}

	protected void printRequestComponents(String filterName, HttpServletRequest httpRequest)
	{
		Logger.log(
				Level.WARNING,
				String.format("Filter '%s' intercepted request:\n" + "Full URL: %s\nDerived servlet path: %s\n",
						filterName,
						HttpRequestUtils.getFullURL(httpRequest),
						HttpRequestUtils.getServletPathWhetherMappedOrNot(httpRequest)
				)
		);
	}
}
