package org.skycrawl.nosqlshowcase.server.root.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.skycrawl.nosqlshowcase.server.Logger;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDataController;
import org.skycrawl.nosqlshowcase.server.root.common.model.WebsiteToCertDataModel;
import org.skycrawl.nosqlshowcase.server.root.ui.notifications.MyNotifications;
import org.skycrawl.nosqlshowcase.server.root.ui.util.IMenuContext;
import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennDiagram;
import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennOverlap;
import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennSet;
import org.skycrawl.nosqlshowcase.server.root.util.CustomOrderSet;

import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;

public class MiniApp<DC extends AbstractDataController<?>> extends Panel
{
	private static final long	serialVersionUID	= -8032034876997584440L;
	
	private static final String TLD_SELECT_MENU_CAPTION = "Show TLD";
	
	private final Map<String, VennDiagram> tldToComponent;
	
	private WebsiteToCertDataModel dataModel;
	private MenuItem currentlySelectedMenuItem;
	
	public MiniApp()
	{
		super();
		
		this.tldToComponent = new HashMap<String,VennDiagram>();
		this.dataModel = new WebsiteToCertDataModel();
		this.currentlySelectedMenuItem = null;
	}

	public void refresh(IMenuContext context, DC dataController)
	{
		/*
		 * First, discard any previous cached data.
		 */
		
		MenuItem tldSelectMenuItem = context.getMenuItemOrCreateNew(TLD_SELECT_MENU_CAPTION);
		clearCache(tldSelectMenuItem);

		/*
		 * Compute the data model and convert it to a series of components categorized by TLD.
		 */
		
		Set<String> tlds = null;
		try
		{
			tlds = dataController.getTLDs();
			this.dataModel = dataController.getSetDomainAndIntersections(tlds);
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
		}
		catch (Exception e)
		{
			Logger.logThrowable("Could not create mini-app: ", e);
			MyNotifications.showError("Mini app could not be launched", e.getLocalizedMessage(), null);
			return;
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