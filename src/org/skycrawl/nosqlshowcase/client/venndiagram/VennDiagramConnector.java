package org.skycrawl.nosqlshowcase.client.venndiagram;

import org.skycrawl.nosqlshowcase.client.jsni.VennIntegration;
import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennDiagram;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.HTML;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(VennDiagram.class)
public class VennDiagramConnector extends AbstractComponentConnector
{
	private static final long	serialVersionUID	= 8895008536121655436L;
	
	// private final VennDiagramServerRpc rpc = RpcProxy.create(VennDiagramServerRpc.class, this);
	private VennIntegration jsIntegration = null;

	public VennDiagramConnector()
	{
		/*
		registerRpc(VennDiagramClientRpc.class, new VennDiagramClientRpc()
		{
			private static final long	serialVersionUID	= 6054718989675309370L;
		});
		*/
	}

	@Override
	protected HTML createWidget()
	{
		return GWT.create(HTML.class);
	}

	@Override
	public HTML getWidget()
	{
		return (HTML) super.getWidget();
	}

	@Override
	public VennDiagramState getState()
	{
		return (VennDiagramState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent)
	{
		super.onStateChanged(stateChangeEvent);

		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
			public void execute()
			{
				// when initial GWT events finish and this component is fully read and published in DOM
				if(jsIntegration == null)
				{
					// prepare DOM structure and Venn integrations script
					getWidget().setHTML("<div id=\"venn-container\"></div><div id=\"venn-tooltip\"></div>");
					jsIntegration = GWT.create(VennIntegration.class);
					jsIntegration.setContainerElementID("venn-container");
					jsIntegration.setTooltipElementID("venn-tooltip");
					jsIntegration.setLeftOffset(getWidget().getAbsoluteLeft());
					jsIntegration.setTopOffset(getWidget().getAbsoluteTop());
					jsIntegration.setWidth(getWidget().getOffsetWidth());
					jsIntegration.setHeight(getWidget().getOffsetHeight());
					
					// transform input data
					JSONArray setsArray = JSONParser.parseStrict(getState().sets).isArray();
					JSONArray overlapsArray = JSONParser.parseStrict(getState().overlaps).isArray();
					
					// take the data and build Venn diagram from them
					jsIntegration.showVenn(false, setsArray.getJavaScriptObject(), overlapsArray.getJavaScriptObject());
				}
			}
		});
	}
}