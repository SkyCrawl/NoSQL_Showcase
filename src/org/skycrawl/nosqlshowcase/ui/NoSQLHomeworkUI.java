package org.skycrawl.nosqlshowcase.ui;

import javax.servlet.annotation.WebServlet;

import redis.RedisConnection;
import redis.RedisContainer;
import riak.RiakConnection;
import riak.RiakContainer;
import riak.RiakUI;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("nosql_homework")
public class NoSQLHomeworkUI extends UI
{
	private VerticalLayout mainLayout;
	private Label dummyMainComponent;
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = NoSQLHomeworkUI.class)
	public static class Servlet extends VaadinServlet
	{
	}

	@Override
	protected void init(VaadinRequest request)
	{
		// some prerequisites
		beforeConstructingUI();

		// and now construct the UI - add components one by one
		addLayer(mainLayout, new Label("Select the database the connect to:"), true);
		addLayer(mainLayout, getDBSwitches(), true);
		addLayer(mainLayout, dummyMainComponent, false);
	}
	
	// ------------------------------------------------------------------------------------------
	// MAIN PAGE COMPONENT BUILDING METHODS
	
	private void beforeConstructingUI()
	{
		// basic UI setup
		dummyMainComponent = new Label("[Click on one of the icons above to connect to a database and start the corresponding mini-app.]");
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSizeUndefined();
		setContent(mainLayout);
		
		// register riak
		RiakConnection riakConnection = new RiakConnection();
		RiakContainer riakContainer = new RiakContainer(riakConnection);
		RiakUI riakUI = new RiakUI(getCurrent(), riakConnection, riakContainer);
		ServerConfig.allDatabases.put(
				"Riak",
				new DatabaseInfo("riak_product_logo_small.png", "localhost", 80, riakConnection, riakContainer, riakUI)
		);
		
		// register redis
		RedisConnection redisConnection = new RedisConnection();
		RedisContainer redisContainer = new RedisContainer(redisConnection);
		// RedisUI redisUI = new RedisUI(getCurrent(), redisConnection, redisContainer);
		ServerConfig.allDatabases.put(
				"Redis",
				new DatabaseInfo("redis_small.png", "localhost", 80, redisConnection, redisContainer, riakUI) // TODO: swap UI for the new one
		);
	}
	
	private Component getDBSwitches()
	{
		HorizontalLayout hLayout = new HorizontalLayout();
		for(String dbName : ServerConfig.allDatabases.keySet())
		{
			addDBSwitch(hLayout, dbName);
		}
		return hLayout;
	}
	
	// ------------------------------------------------------------------------------------------
	// HELPING INTERFACE
	
	private static void addLayer(VerticalLayout layout, Component c, boolean addBottomSpace)
	{
		layout.addComponent(c);
		if(addBottomSpace)
		{
			c.addStyleName("verticalLayoutComponent");
		}
	}
	
	private void addDBSwitch(HorizontalLayout layout, final String dbName)
	{
		Image img = new Image(null, ServerConfig.allDatabases.get(dbName).imgResource);
		Panel panel = new Panel(img);
		panel.setStyleName("dbBanner");
		panel.addClickListener(new MouseEvents.ClickListener()
		{
			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				displayConnectionDialog(dbName);
			}
		});
        layout.addComponent(panel);
	}
	
	private void displayConnectionDialog(final String dbName)
	{
        FormLayout subContent = new FormLayout();
        subContent.setMargin(true);
        
        /*
        Label label = new Label("Pokus");
        label.setStyleName("dbBanner");
        subContent.addComponent(label);
        */
        
        final TextField tHostname = new TextField("", "localhost");
        subContent.addComponent(new Label("Hostname:"));
        subContent.addComponent(tHostname);
        
        final TextField tPort = new TextField("", "80");
        subContent.addComponent(new Label("Port:"));
        subContent.addComponent(tPort);
        
        StaticMethods.createComponentConfirmDialog(getCurrent(), "Connect to " + dbName + " database", subContent, new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				DatabaseInfo newDBInfo = ServerConfig.allDatabases.get(dbName);
				if(newDBInfo == ServerConfig.currentlySelectedDB)
				{
					Notification.show("You are currently connected to this database.");
				}
				else
				{
					try
					{
						// connect to new destination, disconnect from the previous one and update UI (all-in-one call)
						newDBInfo.connect(tHostname.getValue(), Integer.parseInt(tPort.getValue()));
						
						// remove previous main component; don't close current connection if any
						mainLayout.removeComponent(ServerConfig.currentlySelectedDB != null ? ServerConfig.currentlySelectedDB.customDBApplicationUI.getApplicationComponent() : dummyMainComponent);
						
						// add the new main component
						ServerConfig.currentlySelectedDB = newDBInfo;
						addLayer(mainLayout, ServerConfig.currentlySelectedDB.customDBApplicationUI.getApplicationComponent(), false);

						// and show a notification as a result
						Notification.show("Table data source successfully changed!");
					}
					catch (NumberFormatException e)
					{
						Notification.show("Incorret port entered (not a number).");
					}
					catch (Exception e)
					{
						Notification.show(String.format("Could not connect because:\n%s", e.getMessage()));
					}
				}
			}
		});
	}
}