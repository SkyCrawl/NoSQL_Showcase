package org.skycrawl.nosqlshowcase.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;

public class VennIntegration extends JavaScriptObject
{
	/**
	 * Protected Ctor.
	 * Makes JSNI (GWT) happy
	 */
	protected VennIntegration() {}
	
	public final native void setContainerElementID(String id)
	/*-{
		this.elementID_container = id;
	}-*/;
	
	public final native void setTooltipElementID(String id)
	/*-{
		this.elementID_tooltip = id;
	}-*/;
	
	public final native void setLeftOffset(int left)
	/*-{
		this.leftOffset = left;
	}-*/;
	
	public final native void setTopOffset(int top)
	/*-{
		this.topOffset = top;
	}-*/;
	
	public final native void setWidth(int width)
	/*-{
		this.containerWidth = width;
	}-*/;
	
	public final native void setHeight(int height)
	/*-{
		this.containerHeight = height;
	}-*/;
	
	public final native void showVenn(boolean multiDimensional, JavaScriptObject sets, JavaScriptObject overlaps)
	/*-{
		
		// get associated elements using D3 (otherwise Venn will complain) and style them
		var container = $wnd.d3.select("#" + this.elementID_container);
		var tooltip = $wnd.d3.select("#" + this.elementID_tooltip);
		tooltip
			.style("position", "absolute")
			.style("height", 25 + "px")
			.style("line-height", 25 + "px")
			.style("border-radius", 10 + "px")
			.style("padding-left", 20 + "px")
			.style("padding-right", 20 + "px")
			.style("background-color", "black")
			.style("color", "white");
		
		// add a special function to D3
		$wnd.d3.selection.prototype.moveParentToFront = function()
		{
			return this.each(function()
			{
    			this.parentNode.parentNode.appendChild(this.parentNode);
  			});
		};
	
		// get positions for each set
		var data = $wnd.venn.venn(sets, overlaps, multiDimensional ? {layoutFunction: $wnd.venn.classicMDSLayout} : undefined);

		// draw the diagram
		var diagram = $wnd.venn.drawD3Diagram(container, data, this.containerWidth, this.containerHeight);
		var leftOffset = this.leftOffset;
		var topOffset = this.topOffset;
		
		// alter styles
		// var colours = ['black', 'red', 'blue', 'green'];
		diagram.circles
			// .style("fill-opacity", 0) // set fill fully transparent (=> white background)
			.style("stroke-width", 5) // add border
			.style("stroke-opacity", 0)
			.style("stroke", "white");
		diagram.text
			.style("font-size", "1.5em")
            .style("font-weight", "bold");
		
        // make it interactive
        diagram.nodes
        	.on("mousemove", function()
    		{
    			tooltip
    				.style("left", ($wnd.d3.event.pageX - leftOffset) + "px")
               		.style("top", ($wnd.d3.event.pageY - topOffset + 20) + "px");
    		})
        	.on("mouseover", function(d, i)
    		{
    			$wnd.d3.select(this).select("circle").moveParentToFront().transition()
    				.style("fill-opacity", .5)
    				.style("stroke-opacity", 1);
				tooltip.transition().style("opacity", .9);
        		tooltip.text(d.size + " items");
    		})
    		.on("mouseout", function(d, i)
    		{
    			$wnd.d3.select(this).select("circle").transition()
            		.style("fill-opacity", .3)
            		.style("stroke-opacity", 0);
        		tooltip.transition().style("opacity", 0);
    		});
    	
    	// playing with intersection areas	
    	diagram.svg.select("g").selectAll("path").data(overlaps).enter().append("path").attr("d", function(d)
    		{
    			// draw a custom node (SVG path in this case) around them
    			return $wnd.venn.intersectionAreaPath(d.sets.map(function(j)
    			{
    				return sets[j];
    			}));
    		})
    		// change styles and define event handlers
    		.style("fill-opacity", 0)
    		.style("fill", "black")
    		.style("stroke-opacity", 0)
    		.style("stroke", "white")
    		.style("stroke-width", 2)
    		.on("mouseover", function(d, i)
    		{
        		$wnd.d3.select(this).transition()
            		.style("fill-opacity", .1)
            		.style("stroke-opacity", 1);
        		tooltip.transition().style("opacity", .9);
        		tooltip.text(d.size + " items");
    		})
    		.on("mouseout", function(d, i)
    		{
    			$wnd.d3.select(this).transition()
            		.style("fill-opacity", 0)
            		.style("stroke-opacity", 0);
        		tooltip.transition().style("opacity", 0);
    		})
    		.on("mousemove", function() // attach tooltip to the cursor
    		{
    			tooltip
    				.style("left", ($wnd.d3.event.pageX - leftOffset) + "px")
               		.style("top", ($wnd.d3.event.pageY - topOffset + 20) + "px");
    		});
    	
	}-*/;
}