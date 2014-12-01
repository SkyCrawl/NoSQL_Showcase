package org.skycrawl.nosqlshowcase.server.root.db;

import java.io.Serializable;

import org.skycrawl.nosqlshowcase.server.root.util.IVersionsSpecifier;

import com.vaadin.ui.UI;

public interface IDatabaseInfo<CA extends AbstractDatabaseConnection<?,?>> extends Serializable
{
	String getName();
	IVersionsSpecifier getSupportedVersions();
	String getSupportedClient();
	int getDefaultPort();
	String getBannerURL();
	Class<? extends UI> getAssociatedUI();
	Class<CA> getAssociatedConnection();
}