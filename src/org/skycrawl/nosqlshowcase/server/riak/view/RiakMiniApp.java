package org.skycrawl.nosqlshowcase.server.riak.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.skycrawl.nosqlshowcase.server.Logger;
import org.skycrawl.nosqlshowcase.server.riak.controller.RiakDataController;
import org.skycrawl.nosqlshowcase.server.riak.model.RiakWebsite;
import org.skycrawl.nosqlshowcase.server.riak.model.RiakX509Cert;
import org.skycrawl.nosqlshowcase.server.root.ui.notifications.MyNotifications;
import org.skycrawl.nosqlshowcase.server.root.ui.util.IMenuContext;
import org.skycrawl.nosqlshowcase.server.root.ui.util.IMiniApp;
import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennDiagram;
import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennOverlap;
import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennSet;
import org.skycrawl.nosqlshowcase.server.root.util.CustomOrderSet;

import com.basho.riak.client.bucket.Bucket;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;

public class RiakMiniApp extends Panel implements IMiniApp<RiakDataController>
{
	private static final long	serialVersionUID	= -7747811508882278766L;
	
	private static final String TLD_SELECT_MENU_CAPTION = "Show TLD";
	
	private final WebsiteToCertDataModel dataModel;
	private final Map<String, VennDiagram> tldToComponent;
	
	private MenuItem currentlySelectedMenuItem;
	
	public RiakMiniApp()
	{
		super();
		this.dataModel = new WebsiteToCertDataModel();
		this.tldToComponent = new HashMap<String,VennDiagram>();
		this.currentlySelectedMenuItem = null;
	}

	@Override
	public void refresh(IMenuContext context, RiakDataController dataController)
	{
		/*
		 * First, discard any previous cached data.
		 */
		
		MenuItem tldSelectMenuItem = context.getMenuItemOrCreateNew(TLD_SELECT_MENU_CAPTION);
		clearCache(tldSelectMenuItem);

		/*
		 * Compute the data model.
		 */
		
		Set<String> tlds = null;
		try
		{
			// read all relevant buckets
			tlds = dataController.getAllBucketNames();
			tlds.remove(RiakDataController.BUCKET_NAME_CERTIFICATES);
			
			// and turn them into Venn data
			for(String tld : tlds)
			{
				dataModel.registerSet(tld);
				
				Bucket bucket = dataController.getBucket(tld);
				for(String key_website : bucket.keys())
				{
					// fetch the website database entity
					RiakWebsite website = bucket.fetch(key_website, RiakWebsite.class).execute();
					
					// determine root CA for it
					RiakX509Cert rootCA = dataController.linkWalkToRoot(website);
					
					// register the certificate
					dataModel.registerSet(rootCA);
					
					// and register the current found overlap
					dataModel.registerOverlap(key_website, tld, rootCA);
				}
			}
		}
		catch (Exception e)
		{
			Logger.logThrowable("Could not create mini-app: ", e);
			MyNotifications.showError("Mini app could not be launched", e.getLocalizedMessage(), null);
			return;
		}
		
		/*
		 * Convert the data model to a series of components categorized by TLD and
		 * recreate menu items to switch between TLD diagrams.
		 */
		
		tlds = new CustomOrderSet<String>(tlds); // sort TLDs
		for(String tld : tlds)
		{
			List<VennSet> sets = new ArrayList<VennSet>();
			List<VennOverlap> overlaps = new ArrayList<VennOverlap>();
			dataModel.fillDataForTLD(tld, sets, overlaps);
			
			VennDiagram diagram = new VennDiagram(sets, overlaps); // sets list is not empty - at least TLD set must have been exported
			diagram.setSizeFull();
			tldToComponent.put(tld, diagram);
			
			MenuItem item = tldSelectMenuItem.addItem(tld, new MenuBar.Command()
			{
				private static final long	serialVersionUID	= 5011954463489350360L;

				@Override
				public void menuSelected(MenuItem selectedItem)
				{
					currentlySelectedMenuItem.setChecked(false); // should not spawn command event...
					if(selectedItem.isChecked()) // protection against the above line triggering another event like this
					{
						currentlySelectedMenuItem = selectedItem;
						displayContentForCurrentlySelectedMenuItem();
					}
				}
			});
			item.setCheckable(true);
		}
		
		/*
		 * Check the first menu item and display content for it.
		 */
		
		if((tldSelectMenuItem.getChildren() == null) || tldSelectMenuItem.getChildren().isEmpty())
		{
			tldSelectMenuItem.setEnabled(false);
			setContent(new Label("No applicable data was found in the database."));
		}
		else
		{
			tldSelectMenuItem.setEnabled(true);
			currentlySelectedMenuItem = tldSelectMenuItem.getChildren().get(0);
			currentlySelectedMenuItem.setChecked(true); // should not spawn command event...
			displayContentForCurrentlySelectedMenuItem();
		}
	}
	
	private void displayContentForCurrentlySelectedMenuItem()
	{
		setContent(tldToComponent.get(currentlySelectedMenuItem.getText()));
	}
	
	private void clearCache(MenuItem tldSelectMenuItem)
	{
		dataModel.clear();
		tldToComponent.clear();
		tldSelectMenuItem.removeChildren();
	}
}