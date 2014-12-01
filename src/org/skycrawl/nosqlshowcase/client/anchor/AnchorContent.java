package org.skycrawl.nosqlshowcase.client.anchor;

import java.io.Serializable;

public class AnchorContent implements Serializable
{
	private static final long	serialVersionUID	= 4259848845591015349L;
	
	public String text;
	public boolean isHTML;

	/**
	 * Default public constructor keeps Vaadin happy.
	 */
	@Deprecated
	public AnchorContent()
	{
	}
	
	public AnchorContent(String content, boolean isHTML)
	{
		this.text = content;
		this.isHTML = isHTML;
	}

	public String getContent()
	{
		return this.text;
	}

	public void setContent(String content)
	{
		this.text = content;
	}

	public boolean isHTML()
	{
		return this.isHTML;
	}

	public void setHTML(boolean isHTML)
	{
		this.isHTML = isHTML;
	}
}