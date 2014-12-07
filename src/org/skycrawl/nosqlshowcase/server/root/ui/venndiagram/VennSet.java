package org.skycrawl.nosqlshowcase.server.root.ui.venndiagram;

public class VennSet
{
	private String label;
	private int size;
	
	public VennSet(String label, int size)
	{
		this.label = label;
		this.size = size;
	}

	public String getLabel()
	{
		return this.label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public int getSize()
	{
		return this.size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}
}