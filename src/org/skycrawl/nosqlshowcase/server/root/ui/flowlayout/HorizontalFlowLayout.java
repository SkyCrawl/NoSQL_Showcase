package org.skycrawl.nosqlshowcase.server.root.ui.flowlayout;

import org.skycrawl.nosqlshowcase.server.root.ui.util.StyleBuilder;

import com.vaadin.ui.Component;

/**
 * Horizontal flow layout implementation - inner components are placed
 * one after another, each displayed to its full width (may vary).
 * If the element would exceed the width of the container, the element
 * is placed in the next "row" even if the container's height would be
 * exceeded (container doesn't expand to fit the content).
 * 
 * @author SkyCrawl
 */
public class HorizontalFlowLayout extends AbstractFlowLayout
{
	private static final long		serialVersionUID	= 6951568571587805444L;

	/**
	 * Creates a horizontal flow layout with the given style provider for
	 * inner components.
	 */
	public HorizontalFlowLayout(IFlowLayoutStyleProvider styleProvider)
	{
		super(styleProvider);
	}

	/**
	 * Adds the specified component to the right (applies a float: right
	 * property).
	 */
	public void addComponentToRight(Component c)
	{
		super.addComponent(c);
	}

	@Override
	protected String getCss(Component c)
	{
		StyleBuilder builder = new StyleBuilder();
		if (getStyleProvider() != null)
		{
			// attach styles defined outside of this class
			getStyleProvider().setStylesForInnerComponent(c, builder);
		}
		// ensure that the "float" property is set to the right value
		builder.setProperty("float", "left");
		return builder.build();
	}
	
	/*
	private boolean containsComponent(Component c)
	{
		return getComponentIndex(c) != -1;
	}
	*/
}
