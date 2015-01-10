package org.skycrawl.nosqlshowcase.server.root.ui;

import javax.servlet.annotation.WebServlet;

import org.skycrawl.nosqlshowcase.server.Logger;
import org.skycrawl.nosqlshowcase.server.root.requests.HttpRequestComponent;
import org.skycrawl.nosqlshowcase.server.root.requests.HttpRequestUtils;
import org.skycrawl.nosqlshowcase.server.root.ui.notifications.MyFancyNotifications;
import org.skycrawl.nosqlshowcase.server.root.ui.notifications.MyNotifications;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;

@Theme("nosql_showcase")
@Push(value = PushMode.AUTOMATIC)
public abstract class AbstractConfiguredUI extends UI
{
	private static final long	serialVersionUID	= -5534955335099779460L;

	// notifications related variables
	private final MyFancyNotifications notifications = new MyFancyNotifications();
	
	// actual UI structure
	private final CssLayout topLayout = new CssLayout();
	
	// miscellaneous
	private final UniversalUIExtension universalUIExt = new UniversalUIExtension();
	
	@WebServlet(value = { "/*", "/VAADIN/*" }, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = AbstractConfiguredUI.class, widgetset = "org.skycrawl.nosqlshowcase.MasterWidgetset")
	public static class Servlet extends AbstractConfiguredUIServlet
	{
		private static final long	serialVersionUID	= -6166987310102807104L;
	}
	
	// ------------------------------------------------------------------------------------------
	// MAIN INTERFACE
	
	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * Make inner layout independent of the content's size.
		 */
		topLayout.setSizeFull();
		
		/*
		 * Enable sending client errors to the server (where they will be logged).
		 */
		universalUIExt.extend(this);

		/*
		 * Prevent errors from directly being displayed on the client. Rather
		 * log the errors on server and send a notification to the client.
		 * 
		 * NOTE: this is the last hook to catch the exception. If another error
		 * handler is set on a child component of a UI, this error handler will
		 * not be used.
		 */
		setErrorHandler(new DefaultErrorHandler()
		{
			private static final long serialVersionUID = -4395506937938101756L;

			@Override
			public void error(com.vaadin.server.ErrorEvent event)
			{
				Logger.logThrowable("Default UI error handler caught the following error:", event.getThrowable());
				MyNotifications.showApplicationError();
			}
		});
		
		/*
		 * Build the main UI, common for all database related apps.
		 */
		buildUI();
	}
	
	@Override
	public void setContent(Component content)
	{
		/*
		 * Reserved for setting the child content.
		 */
		setMyContent(content);
	}
	
	/**
	 * <p>A special primary routine for setting this UI's content. All methods in
	 * this class should use this method instead of the inherited
	 * {@link #setContent()} which is now solely dedicated to the child classes.</p>
	 * 
	 * <p>REASON: The notifications feature has a tendency to cause a lot of
	 * confusion because the notifications component has to always be present in
	 * UI's layout and this should be done transparently...</p>
	 * 
	 */
	private void setMyContent(Component content)
	{
		if (content == null) // most likely Vaadin's own initialization, unfortunately...
		{
			/*
			 * We have to forward the call and return because nothing is
			 * initialized at this point. This method has been called from super
			 * constructor.
			 */
			super.setContent(content);
		}
		else
		{
			topLayout.removeAllComponents();
			topLayout.addComponent(content);
			content.setSizeFull();
			topLayout.addComponent(notifications); // always ensure that notifications component is added
			super.setContent(topLayout); // the method is overriden in these class - let's avoid an infinite loop
		}
	}
	
	public MyFancyNotifications getNotificationsComponent()
	{
		return notifications;
	}
	
	public UniversalUIExtension getUniversalExtension()
	{
		return universalUIExt;
	}
	
	// ------------------------------------------------------------------------------------------
	// UI BUILDING INTERFACE
	
	protected abstract void buildUI();
	
	// ------------------------------------------------------------------------------------------
	// CONVENIENCE INTERFACE
	
	protected void setPageCroppedAndHorizontallyCentered(boolean centered)
	{
		if (centered)
		{
			topLayout.setSizeUndefined();
			topLayout.setStyleName("rootAppLayout");
		}
		else
		{
			topLayout.removeStyleName("rootAppLayout");
			topLayout.setSizeFull();
		}
	}
	
	/**
	 * Returns something like "http://my.domain/Pikater". A URL that can be used
	 * to access various application artifacts or servlets.
	 */
	public static String getBaseAppURLFromLastRequest()
	{
		return HttpRequestUtils.getURLPrefix(VaadinServletService.getCurrentServletRequest(), HttpRequestComponent.P4_APPCONTEXT);
	}
	
	/**
	 * Returns something like "http://my.domain/application/servlet".
	 * 
	 * @param newServletPath
	 * @return
	 */
	public static String getRedirectURL(String newServletPath)
	{
		return getBaseAppURLFromLastRequest() + "/" + newServletPath;
	}
	
	public static boolean isProductionModeActive()
	{
		return VaadinSession.getCurrent().getConfiguration().isProductionMode();
	}

	public static boolean isDebugModeActive()
	{
		return !isProductionModeActive();
	}
}