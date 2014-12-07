package org.skycrawl.nosqlshowcase.server.root.ui.util;

import com.vaadin.ui.MenuBar.MenuItem;

public interface IMenuContext
{
	MenuItem getMenuItemOrCreateNew(String caption);
}