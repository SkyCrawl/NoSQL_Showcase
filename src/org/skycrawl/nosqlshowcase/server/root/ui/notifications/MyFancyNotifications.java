package org.skycrawl.nosqlshowcase.server.root.ui.notifications;

import org.vaadin.alump.fancylayouts.FancyNotification;
import org.vaadin.alump.fancylayouts.FancyNotifications;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyNotificationsState.Position;

import com.vaadin.annotations.StyleSheet;

/**
 * Our own configured version of notifications
 * take from the FancyNotifications Vaadin add-on.
 * 
 * @author SkyCrawl
 */
@StyleSheet("myFancyNotifications.css")
public class MyFancyNotifications extends FancyNotifications
{
	private static final long	serialVersionUID	= 3423385555186912646L;

	public MyFancyNotifications()
	{
		super();

		setPosition(Position.TOP_RIGHT);
		setCloseTimeout(7500); // 7,5 seconds
		setClickClose(true); // click closes notifications
		// notifications.setDefaultIcon(new ThemeResource("images/vaadin.png"));
		addListener(new FancyNotifications.NotificationsListener()
		{
			@Override
			public void notificationClicked(Object id)
			{
				if(id != null)
				{
					MyNotifications.notificationClicked((Integer) id);
				}
			}
		});
	}
	
	@Override
	public FancyNotification getNotification(Object id)
	{
		return (FancyNotification) super.getNotification(id);
	}
}