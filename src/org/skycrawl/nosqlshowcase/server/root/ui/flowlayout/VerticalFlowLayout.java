package org.skycrawl.nosqlshowcase.server.root.ui.flowlayout;

import org.skycrawl.nosqlshowcase.server.root.ui.util.StyleBuilder;

import com.vaadin.ui.Component;

/**
 * Vertical flow layout implementation - inner components are placed
 * one under another, each displayed to its full height (may vary).
 * If the element would exceed the height of the container, the element
 * is placed in the next "column" even if the container's width would be
 * exceeded (container doesn't expand to fit the content).
 * 
 * @author SkyCrawl
 */
public class VerticalFlowLayout extends AbstractFlowLayout
{
	private static final long	serialVersionUID	= 7467817672610031711L;

	/**
	 * Creates a vertical flow layout with the given style provider for
	 * inner components.
	 */
	public VerticalFlowLayout(IFlowLayoutStyleProvider styleProvider)
	{
		super(styleProvider);
	}

	@Override
	protected String getCss(Component c)
	{
		StyleBuilder builder = new StyleBuilder();
		if (getStyleProvider() != null)
		{
			getStyleProvider().setStylesForInnerComponent(c, builder);
		}
		builder.setProperty("display", "block");
		return builder.build();
	}
}