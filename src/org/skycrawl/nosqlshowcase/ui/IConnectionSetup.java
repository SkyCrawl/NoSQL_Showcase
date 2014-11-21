package org.skycrawl.nosqlshowcase.ui;

public interface IConnectionSetup
{
	boolean connect(String currentHostname, int currentPort) throws Exception;
}
