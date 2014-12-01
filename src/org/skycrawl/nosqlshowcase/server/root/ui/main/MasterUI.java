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

@Title("NoSQL Showcase")
public class MasterUI extends AbstractConfiguredUI
{
	private static final long	serialVersionUID	= -2577799316278280360L;

	@Override
	protected void buildUI()
	{
		// first and foremost:
		addStyleName("master-ui");

		// define content
		HorizontalFlowLayout hfl = new HorizontalFlowLayout(new IFlowLayoutStyleProvider()
		{
			@Override
			public void setStylesForInnerComponent(Component c, StyleBuilder builder)
			{
				builder.setProperty("margin", "25px 50px 25px 0px");
			}
		});
		hfl.setSizeFull();
		hfl.setCaption("Click on a database logo to open its corresponding mini-app:");
		hfl.setStyleName("master-ui");
		
		for(final Entry<String, DatabaseHandle<?>> entry : Config.getSupportedDatabases().entrySet())
		{
			String clickAction = String.format("javascript:window.open('%s', '_blank');", getRedirectURL(entry.getKey()));
			String dbLogoRelativeURL = ThemeResources.getVaadinRelativePathForResource(entry.getValue().getStaticInformation().getBannerURL()); 
			Label lbl_banner = new Label(String.format("<a href=\"%s\"><img src=\"%s\"></a>", clickAction, dbLogoRelativeURL), ContentMode.HTML);
			lbl_banner.setSizeUndefined();
			lbl_banner.setStyleName("dbLogo");
			hfl.addComponent(lbl_banner);
		}
		
		// and finish up
		setPageCroppedAndHorizontallyCentered(true);
		setContent(hfl);
	}
}