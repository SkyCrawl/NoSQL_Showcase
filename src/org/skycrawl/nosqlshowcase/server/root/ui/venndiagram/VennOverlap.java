package org.skycrawl.nosqlshowcase.server.root.ui.venndiagram;

public class VennOverlap
{
	private int[] sets;
	private int size;
	
	public VennOverlap(int[] sets, int size)
	{
		this.sets = sets;
		this.size = size;
	}

	public int[] getSets()
	{
		return this.sets;
	}

	public void setSets(int[] sets)
	{
		this.sets = sets;
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