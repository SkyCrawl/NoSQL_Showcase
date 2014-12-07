package org.skycrawl.nosqlshowcase.server.root.ui.venndiagram;

import java.util.List;

import org.skycrawl.nosqlshowcase.client.venndiagram.VennDiagramServerRpc;
import org.skycrawl.nosqlshowcase.client.venndiagram.VennDiagramState;
import org.skycrawl.nosqlshowcase.server.root.util.MyJSONUtils;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractComponent;

@JavaScript({ "d3/d3.min.js", "venn/venn.js", "venn/numeric-1.2.6.min.js", "mds/mds.js" })
public class VennDiagram extends AbstractComponent
{
	private static final long	serialVersionUID	= -8486664004180664525L;
	
	private VennDiagramServerRpc rpc;  

	public VennDiagram(List<VennSet> sets, List<VennOverlap> overlaps)
	{
		this.rpc = new VennDiagramServerRpc()
		{
			private static final long	serialVersionUID	= 5498754893309951847L;
		};
		registerRpc(rpc);
		
		getState().sets = MyJSONUtils.toJson(sets);
		getState().overlaps = MyJSONUtils.toJson(overlaps);
		
		// System.out.println(MyAutoBeanFactory.toJson(sets));
		// System.out.println(MyAutoBeanFactory.toJson(overlaps));
	}

	@Override
	public VennDiagramState getState()
	{
		return (VennDiagramState) super.getState();
	}
}