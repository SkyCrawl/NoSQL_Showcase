package org.skycrawl.nosqlshowcase.riak;

import org.skycrawl.nosqlshowcase.ui.IMyUI;
import org.skycrawl.nosqlshowcase.ui.StaticMethods;

import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakRetryFailedException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class RiakUI implements IMyUI
{
	// UI
	private final VerticalLayout mainComponent;
	private final ComboBox selectableListOfBuckets;
	private final Table mainTable;
	
	// misc
	private final RiakConnection connection;
	private final RiakContainer dataSource;
	private boolean dataSourceSet; 
	
	@SuppressWarnings("serial")
	public RiakUI(final UI parentUI, final RiakConnection connection, final RiakContainer dataSource)
	{
		this.connection = connection;
		this.dataSource = dataSource;
		this.dataSourceSet = false;
		
		this.selectableListOfBuckets = new ComboBox("Select bucket:");
		this.selectableListOfBuckets.addValueChangeListener(new ValueChangeListener()
		{
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				String newValue = (String) event.getProperty().getValue();
				if(dataSource.setContext(newValue)) // a different bucket has been selected
				{
					mainTable.refreshRowCache();
				}
			}
		});
		
		HorizontalLayout hLayout = new HorizontalLayout();
		final TextField tNewBucket = new TextField("Create bucket:"); 
		hLayout.addComponent(tNewBucket);
		hLayout.addComponent(new Button("Create", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				final String newBucket = tNewBucket.getValue();
				if(!selectableListOfBuckets.containsId(newBucket))
				{
					try
					{
						connection.createBucket(newBucket);
						
						StaticMethods.createTextConfirmDialog(parentUI, "Switch to the new bucket?", new ClickListener()
						{
							@Override
							public void buttonClick(ClickEvent event)
							{
								dataSource.setContext(newBucket);
								mainTable.refreshRowCache();
							}
						});
					}
					catch (RiakRetryFailedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					Notification.show(String.format("Bucket '%s' already exists.", newBucket)); 
				}
			}
		}));
		
		this.mainTable = new Table("Items in selected bucket:");
		this.mainTable.setMultiSelect(true);
		
		this.mainComponent = new VerticalLayout();
		this.mainComponent.setSpacing(true);
		this.mainComponent.addComponent(this.selectableListOfBuckets);
		this.mainComponent.addComponent(this.mainTable);
		this.mainComponent.addComponent(hLayout);
	}

	@Override
	public Component getApplicationComponent()
	{
		return mainComponent;
	}
	
	@Override
	public void newConnectionEstablished()
	{
		selectableListOfBuckets.removeAllItems();
		try
		{
			for(String bucket : connection.getBuckets())
			{
				selectableListOfBuckets.addItem(bucket);
			}
		}
		catch (UnsupportedOperationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (RiakException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!dataSourceSet)
		{
			mainTable.setContainerDataSource(dataSource);
		}
	}
}
