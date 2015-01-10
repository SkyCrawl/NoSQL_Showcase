package org.skycrawl.nosqlshowcase.server.root.ui.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.skycrawl.nosqlshowcase.server.Config;
import org.skycrawl.nosqlshowcase.server.Logger;
import org.skycrawl.nosqlshowcase.server.root.db.AbstractDatabaseConnection;
import org.skycrawl.nosqlshowcase.server.root.db.DatabaseHandle;
import org.skycrawl.nosqlshowcase.server.root.requests.HttpRequestUtils;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.UI;

public class AbstractConfiguredUIServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener
{
	private static final long					serialVersionUID	= -8268135994612358127L;

	// --------------------------------------------------------
	// INHERITED INTERFACE

	@Override
	protected void servletInitialized() throws ServletException
	{
		super.servletInitialized(); // this should always be called first

		getService().addSessionInitListener(this);
		getService().addSessionDestroyListener(this);
	}

	@Override
	protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException
	{
		VaadinServletService vaadinServletService = new VaadinServletService(this, deploymentConfiguration)
		{
			private static final long	serialVersionUID	= 8833521650509773424L;

			@Override
			protected List<RequestHandler> createRequestHandlers() throws ServiceException
			{
				/*
				 * Specify request handlers in the order they will be called
				 * (LIFO) on incoming requests. This is useful for constructing
				 * dynamic content where no UIs are necessary.
				 */
				List<RequestHandler> requestHandlerList = super.createRequestHandlers();
				// requestHandlerList.add(new RequestHandler());
				return requestHandlerList;
			}
		};
		vaadinServletService.init();
		return vaadinServletService;
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException
	{
		event.getSession().addUIProvider(new UIProvider()
		{
			private static final long	serialVersionUID	= -1327822157863022893L;

			@Override
			public Class<? extends UI> getUIClass(UIClassSelectionEvent event)
			{
				String servletPath = getCurrentServletPath();
				if(Config.getDefaultServletPath().equals(servletPath))
				{
					return Config.getDefaultUI();
				}
				else
				{
					DatabaseHandle<?> mappedDatabaseInfo = getCurrentDatabaseInfo(servletPath);
					if(mappedDatabaseInfo == null)
					{
						try
						{
							VaadinServletService.getCurrentResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
						}
						catch (IOException e)
						{
							Logger.logThrowable(String.format("An undefined resource with servlet path '%s' was requested "
									+ "but writing an error code of 404 (NOT_FOUND) to the response failed because of the "
									+ "following exception. Vaadin should have defaulted to error code 500 instead.", getCurrentServletPath()), e);
						}
						return null;
					}
					else
					{
						return mappedDatabaseInfo.getStaticInformation().getAssociatedUI();
					}
				}
			}
		});
	}

	@Override
	public void sessionDestroy(SessionDestroyEvent event)
	{
		/*
		 * BACKGROUND:
		 * UIs are bound to a VaadinSession. We don't need to close and destroy them because
		 * that is already done for us automatically (detach listeners).
		 * A UI expires when no requests are received by it from the client and after the client
		 * engine sends 3 keep-alive messages. All UIs of a session expire => session still remains
		 * and is cleaned up from the server when the session timeout configured in the web application
		 * expires.
		 */
		
		// close all DB connections
		for(DatabaseHandle<?> dbHandle : Config.getSupportedDatabases().values())
		{
			dbHandle.getConnectionHandle().close();
		}
	}
	
	public static String getCurrentServletPath()
	{
		return HttpRequestUtils.getServletPathWhetherMappedOrNot(VaadinServletService.getCurrentServletRequest());
	}
	
	/**
	 * Type safety is ensured with 3 conditions:
	 * <ol>
	 * <li> Use servlet paths and {@link UIProvider} to make sure the correct database handle is 
	 * accessed in descendants of {@link AbstractDatabaseUI}. This is taken care of in
	 * {@link AbstractConfiguredUIServlet}.
	 * <li> Get/set each database handle ONLY from within the corresponding {@link AbstractDatabaseUI},
	 * which is typed.
	 * <li> Correct typing of descendants of {@link AbstractDatabaseUI} and {@link AbstractDatabaseConnection}.
	 * </ol>
	 *
	 * @return
	 */
	public static <CA extends AbstractDatabaseConnection<?,?>> DatabaseHandle<CA> getCurrentDatabaseInfo()
	{
		return getCurrentDatabaseInfo(getCurrentServletPath());
	}
	
	@SuppressWarnings("unchecked")
	private static <CA extends AbstractDatabaseConnection<?,?>> DatabaseHandle<CA> getCurrentDatabaseInfo(String servletPath)
	{
		return (DatabaseHandle<CA>) Config.getSupportedDatabases().get(servletPath);
	}
}