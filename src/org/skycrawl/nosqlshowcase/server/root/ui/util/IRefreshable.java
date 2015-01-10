package org.skycrawl.nosqlshowcase.server.root.ui.util;

import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDataController;

public interface IRefreshable<DC extends AbstractDataController<?>>
{
	void refresh(IMenuContext context, DC dataController);
}