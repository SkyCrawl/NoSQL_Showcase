package org.skycrawl.nosqlshowcase.server.root.ui.util;

import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDataController;

import com.vaadin.ui.Component;

public interface IMiniApp<DC extends AbstractDataController<?>> extends Component, IRefreshable<DC>
{
}