package org.skycrawl.nosqlshowcase.client.extensions;

import org.skycrawl.nosqlshowcase.client.gwtmanagers.GWTLogger;
import org.skycrawl.nosqlshowcase.server.root.ui.UniversalUIExtension;

import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/** 
 * @author SkyCrawl 
 */
@Connect(UniversalUIExtension.class)
public class UniversalUIExtensionConnector extends AbstractExtensionConnector
{
	private static final long serialVersionUID = 6766120104518020715L;

	private final UniversalUIExtensionServerRpc serverRPC = RpcProxy.create(UniversalUIExtensionServerRpc.class, this);

	public UniversalUIExtensionConnector()
	{
		/*
		registerRpc(UniversalUIExtensionClientRpc.class, new UniversalUIExtensionClientRpc()
		{
			private static final long	serialVersionUID	= 3288019886099574976L;
		});
		*/
	}

	@Override
	protected void extend(ServerConnector target)
	{
		if (!GWTLogger.isLoggerSet())
		{
			GWTLogger.setRemoteLogger(serverRPC);
		}
	}
}