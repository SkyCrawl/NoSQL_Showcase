package org.skycrawl.nosqlshowcase.ui;

public interface IMyContainer
{
	/**
	 * Set the context with the right parameter and then call refresh on the parent UI component if the context is different
	 * from the previous one (if this method returns true).
	 * @param newContext
	 * @return
	 */
	boolean setContext(Object newContext);
}
