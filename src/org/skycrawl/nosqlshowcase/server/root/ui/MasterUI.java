package org.skycrawl.nosqlshowcase.server.root.ui.main;

import java.util.Map.Entry;

import org.skycrawl.nosqlshowcase.server.Config;
import org.skycrawl.nosqlshowcase.server.ThemeResources;
import org.skycrawl.nosqlshowcase.server.root.db.DatabaseHandle;
import org.skycrawl.nosqlshowcase.server.root.ui.flowlayout.HorizontalFlowLayout;
import org.skycrawl.nosqlshowcase.server.root.ui.flowlayout.IFlowLayoutStyleProvider;
import org.skycrawl.nosqlshowcase.server.root.ui.util.StyleBuilder;

import com.vaadin.annotations.Title;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@Title("NoSQL Showcase")
public class MasterUI extends AbstractConfiguredUI
{
	private static final long	serialVersionUID	= -2577799316278280360L;

	@Override
	protected void buildUI()
	{
		/*
		 * First and foremost:
		 */
		// addStyleName("master-ui"); // not needed at the moment

		/*
		 * Define content.
		 */
		HorizontalFlowLayout fLayout_dbLogo = new HorizontalFlowLayout(new IFlowLayoutStyleProvider()
		{
			@Override
			public void setStylesForInnerComponent(Component c, StyleBuilder builder)
			{
				builder.setProperty("margin", "25px 50px 25px 0px");
			}
		});
		fLayout_dbLogo.setSizeFull();
		fLayout_dbLogo.setStyleName("dbLogoLayout");
		fLayout_dbLogo.setCaption("Click on a database logo to open its corresponding mini-app:");
		
		for(final Entry<String, DatabaseHandle<?>> entry : Config.getSupportedDatabases().entrySet())
		{
			String clickAction = String.format("javascript:window.open('%s', '_blank');", getRedirectURL(entry.getKey()));
			String dbLogoRelativeURL = ThemeResources.getVaadinRelativePathForResource(entry.getValue().getStaticInformation().getBannerURL()); 
			Label lbl_banner = new Label(String.format("<a href=\"%s\"><img src=\"%s\"></a>", clickAction, dbLogoRelativeURL), ContentMode.HTML);
			lbl_banner.setSizeUndefined();
			lbl_banner.setStyleName("dbLogo");
			fLayout_dbLogo.addComponent(lbl_banner);
		}
		
		/*
		 * Bring it all together...
		 */
		VerticalLayout vLayout_master = new VerticalLayout();
		vLayout_master.setSizeFull();
		// addTestButton(vLayout_master); // ONLY FOR DEVELOPMENT
		vLayout_master.addComponent(fLayout_dbLogo);
		
		setPageCroppedAndHorizontallyCentered(true);
		setContent(vLayout_master);
	}
	
	/*private void addTestButton(final VerticalLayout masterLayout)
	{
		masterLayout.addComponent(new Button("Test it", new Button.ClickListener()
		{
			private static final long	serialVersionUID	= 1412297752787707103L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				// nothing to test atm
			}
		}));
	}*/
}