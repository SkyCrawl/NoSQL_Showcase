package org.skycrawl.nosqlshowcase.client.venndiagram;

public class VennDiagramState extends com.vaadin.shared.AbstractComponentState
{
	private static final long	serialVersionUID	= -8412892550696221490L;
	
	/**
	 * A JSON array of JSON-formatted sets. Must have the following format: 
	 * <code>[{label: "A", size: 10}, {label: "B", size: 10}]</code>
	 */
	public String sets = null;
	
	/**
	 * A JSON array of JSON-formatted overlaps. Must have the following format:
	 * <code>[{sets: [0,1], size: 2}, {sets: [0,2], size: 1}]</code>
	 */
	public String overlaps = null;
}