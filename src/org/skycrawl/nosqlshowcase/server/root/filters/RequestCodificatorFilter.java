package org.skycrawl.nosqlshowcase.server.root.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.skycrawl.nosqlshowcase.server.Config;

@WebFilter(filterName = "filter1", description = "Looks at incoming request URLs and if needed, appends "
		+ "servlet path pointing to the default (master) UI. To achieve this, a client redirect is sent "
		+ "so that the client browser's address bar reflects the change and bookmarking works properly."
		+ "Example: 'localhost:8080/my-app' => 'localhost:8080/my-app/index'.")
public class RequestCodificatorFilter extends AbstractFilter
{
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException
	{
		// determine basic information & references
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		
		// actually do stuff
		if(!isServletPathDefined(httpRequest))
		{
			clientRedirect(servletResponse, Config.getDefaultServletPath());
		}
		else
		{
			chain.doFilter(servletRequest, servletResponse); // pass the request along the filter chain
		}
	}
}