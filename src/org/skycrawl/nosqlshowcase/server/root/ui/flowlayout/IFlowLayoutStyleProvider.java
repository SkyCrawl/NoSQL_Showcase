package org.skycrawl.nosqlshowcase.server.root.ui.flowlayout;

import org.skycrawl.nosqlshowcase.server.root.ui.util.StyleBuilder;

import com.vaadin.ui.Component;

/**
 * This interface represents a dynamic style mapping to various
 * components within a single container. Avoids having to define
 * a lot of style names and individual style sheets.
 * 
 * @author SkyCrawl
 */
public interface IFlowLayoutStyleProvider
{
	/**
	 * Sets styles to be applied to the given compoment via
	 * a {@link StyleBuilder} instance.
	 */
	void setStylesForInnerComponent(Component c, StyleBuilder builder);
}
