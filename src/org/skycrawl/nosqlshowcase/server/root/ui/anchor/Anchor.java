package org.skycrawl.nosqlshowcase.server.root.ui.anchor;

import org.skycrawl.nosqlshowcase.client.anchor.AnchorClientRpc;
import org.skycrawl.nosqlshowcase.client.anchor.AnchorContent;
import org.skycrawl.nosqlshowcase.client.anchor.AnchorServerRpc;
import org.skycrawl.nosqlshowcase.client.anchor.AnchorState;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponent;

/**
 * A multi-purpose anchor element (e.g. {@literal <a></a>}). Can be used to:
 * <ol>
 * <li>Display an anchor that handles click events on the server.
 * <li>Display an anchor that executes custom javascript code when clicked (and
 * thus acts as a bookmarklet).
 * </ol>
 * 
 * @author SkyCrawl
 */
@StyleSheet("anchor.css")
public class Anchor extends AbstractComponent implements AnchorClientRpc
{
	private static final long	serialVersionUID	= 2302390089747233688L;

	/**
	 * Creates an anchor that handles click events on the server.
	 */
	public Anchor(final ClickListener listener)
	{
		super();
		setPrimaryStyleName("v-widget anchor");
		addStyleName("v-label");

		registerRpc(new AnchorServerRpc()
		{
			private static final long	serialVersionUID	= 8647290803389019408L;

			@Override
			public void clicked(MouseEventDetails mouseDetails)
			{
				if(listener != null)
				{
					listener.click(new ClickEvent(Anchor.this, mouseDetails));
				}
			}
		});
		getState().forwardClickToServer = true;

		/*
		 * TODO: everything is working now but for the sake of nicer
		 * implementation... use a default JS function that does nothing
		 * but DOESN'T let the browser refresh the page (href attribute is
		 * responsible for that and it needs some kind of an empty command).
		 */
	}

	/**
	 * Creates an anchor that executes custom javascript code when clicked.
	 * 
	 * @param caption
	 *        the title of the anchor
	 * @param functionSource
	 *        the javascript code to execute when user clicks the anchor, e.g.:
	 *        <ul>
	 *        <li> <code>alert('Hello world!');</code>
	 *        <li> <code>function(){ alert('Hello world!'); }</code>
	 *        </ul>
	 * @param bookmarklet
	 *        true to make this anchor a bookmarklet
	 */
	public Anchor(String functionSource)
	{
		super();
		setPrimaryStyleName("v-widget v-label anchor");

		if ((functionSource == null) || !functionSource.startsWith("function"))
		{
			getState().hrefAttrContent = String.format("javascript:%s",
					functionSource);
		}
		else
		{
			getState().hrefAttrContent = String.format("javascript:(%s)();",
					functionSource);
		}
	}

	public AnchorContent getValue()
	{
		return getState().content;
	}

	public void setValue(AnchorContent content)
	{
		getState().content = content;
	}

	@Override
	protected AnchorState getState()
	{
		return (AnchorState) super.getState();
	}

	@SuppressWarnings("unused")
	private AnchorClientRpc getClientRPC()
	{
		return getRpcProxy(AnchorClientRpc.class);
	}
}
