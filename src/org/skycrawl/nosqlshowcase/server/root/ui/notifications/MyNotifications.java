package org.skycrawl.nosqlshowcase.server.root.ui.notifications;

import java.util.HashMap;
import java.util.Map;

import org.skycrawl.nosqlshowcase.server.ThemeResources;
import org.skycrawl.nosqlshowcase.server.root.ui.main.AbstractConfiguredUI;
import org.skycrawl.nosqlshowcase.server.root.util.SimpleIDGenerator;
import org.vaadin.alump.fancylayouts.FancyNotification;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.UI;

/**
 * <p>
 * This class define routines for displaying various kinds of notifications in
 * OS X style.</br> Note that this class can not be used outside of Vaadin
 * requests (threads processing them). The methods defined here work on a so
 * called thread-local pattern.
 * </p>
 * <p>
 * TODO: is there a decent way to improve this? To get an instance of underlying
 * UI based on something other than currently running thread? Maybe
 * {@link ResourceRegistrar}?
 * </p>
 * 
 * @author SkyCrawl
 */
public class MyNotifications
{
	// ----------------------------------------------------------------
	// FIELDS AND INTERFACE DEALING WITH NOTIFICATION CLICKS
	
	private static Map<Integer, ClickListener> clickHandlerRegistrar = new HashMap<Integer, ClickListener>();
	private static SimpleIDGenerator idGenerator = new SimpleIDGenerator();
	
	public static void notificationClicked(Integer id)
	{
		clickHandlerRegistrar.get(id).click(null);
	}
	
	// ----------------------------------------------------------------
	// GENERAL USE NOTIFICATIONS

	public static void showSuccess(String title, String description, ClickListener clickListener)
	{
		showNotification(title, description, new ThemeResource(ThemeResources.RELPATH_IMG_NOTIFICATIONS_SUCCESS), clickListener);
	}

	public static void showInfo(String title, String description, ClickListener clickListener)
	{
		showNotification(title, description, new ThemeResource(ThemeResources.RELPATH_IMG_NOTIFICATIONS_INFO), clickListener);
	}

	public static void showWarning(String title, String description, ClickListener clickListener)
	{
		showNotification(title, description, new ThemeResource(ThemeResources.RELPATH_IMG_NOTIFICATIONS_WARN), clickListener);
	}

	public static void showError(String title, String description, ClickListener clickListener)
	{
		showNotification(title, description, new ThemeResource(ThemeResources.RELPATH_IMG_NOTIFICATIONS_ERROR), clickListener);
	}

	// ----------------------------------------------------------------
	// SPECIFIC USE NOTIFICATIONS

	public static void showApplicationError()
	{
		showApplicationError("Please contact the administrators.");
	}

	public static void showApplicationError(String message)
	{
		showError("Application error", message, null);
	}

	// ----------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private static void showNotification(String title, String description, Resource icon, ClickListener clickListener)
	{
		final Integer id = clickListener == null ? null : idGenerator.getAndIncrement();
		getCurrentNotificationsManager().showNotification(id, title, description, icon);
		if(id != null)
		{
			clickHandlerRegistrar.put(id, clickListener);
			getCurrentNotificationsManager().getNotification(id).addDetachListener(new DetachListener()
			{
				private static final long	serialVersionUID	= 8300262617532316631L;

				@Override
				public void detach(DetachEvent event)
				{
					clickHandlerRegistrar.remove(id);
				}
			});
		}
	}

	private static MyFancyNotifications getCurrentNotificationsManager()
	{
		/*
		 * This is one of the reasons why notifications need to operate
		 * on a thread-local pattern - they require to have a special
		 * component attached to a UI.
		 */
		return ((AbstractConfiguredUI) UI.getCurrent()).getNotificationsComponent();
	}

	@SuppressWarnings("unused")
	private static void showHTMLNotification(String title, String message, String styleName)
	{
		FancyNotification notif = new FancyNotification(
				null,
				"Hello <span style=\"text-decoration: underline;\">World</span>",
				"Lorem <span style=\"font-size: 80%;\">ipsum.</span>");
		notif.getTitleLabel().setContentMode(ContentMode.HTML);
		notif.getDescriptionLabel().setContentMode(ContentMode.HTML);
		getCurrentNotificationsManager().showNotification(notif);
	}
}