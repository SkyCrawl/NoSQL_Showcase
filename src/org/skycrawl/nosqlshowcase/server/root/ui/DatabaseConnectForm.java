package org.skycrawl.nosqlshowcase.server.root.ui.main;

import java.net.InetAddress;
import java.util.List;
import java.util.Locale;

import org.skycrawl.nosqlshowcase.server.root.db.DatabaseHandle;
import org.skycrawl.nosqlshowcase.server.root.ui.dialogs.DialogCommons.IDialogResultPreparer;
import org.skycrawl.nosqlshowcase.server.root.ui.dialogs.GeneralDialogs;

import com.google.common.net.HostSpecifier;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class DatabaseConnectForm extends FormLayout implements IDialogResultPreparer
{
	private static final long	serialVersionUID	= 2547937489029543636L;
	
	private final TextField tf_hostname;
	private final TextField tf_port;
	
	public DatabaseConnectForm(DatabaseHandle<?> dbInfo)
	{
		super();
		setWidth("750px"); // TODO: this is ignored... perhaps add a default component to UI so that the window may be centered correctly?
		setMargin(true);
		setSpacing(true);
		
		String hostname = dbInfo.getConnectionHandle().isHostnameDefined() ? dbInfo.getConnectionHandle().getHostname() : InetAddress.getLoopbackAddress().getHostAddress();
		int port = dbInfo.getConnectionHandle().isPortDefined() ? dbInfo.getConnectionHandle().getPort() : dbInfo.getStaticInformation().getDefaultPort(); 
		
		tf_hostname = new TextField("Hostname:", hostname);
		tf_hostname.setSizeFull();
		tf_hostname.setInputPrompt("ipv4, ipv6 or hostname");
		tf_hostname.setRequired(true);
		tf_hostname.setValidationVisible(true);
		
		tf_port = new TextField("Port:", String.valueOf(port));
		tf_port.setSizeFull();
		tf_port.setInputPrompt("database connection port");
		tf_port.setRequired(true);
		tf_port.setValidationVisible(true);
		tf_port.setConverter(new Converter<String, Integer>()
		{
			private static final long	serialVersionUID	= 1526975507560183917L;

			@Override
			public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws ConversionException
			{
				try
				{
					return Integer.parseInt(value);
				}
				catch (NumberFormatException e)
				{
					throw new ConversionException();
				}
			}

			@Override
			public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws ConversionException
			{
				return String.valueOf(value);
			}

			@Override
			public Class<Integer> getModelType()
			{
				return Integer.class;
			}

			@Override
			public Class<String> getPresentationType()
			{
				return String.class;
			}
		});
		
		Label lbl_supportedVersion = new Label(String.format("<i>Currently supported versions: %s.</i>", dbInfo.getStaticInformation().getSupportedVersions().getConciseString()), ContentMode.HTML);
		lbl_supportedVersion.setSizeFull();
		
		addComponents(tf_hostname, tf_port, lbl_supportedVersion);
	}

	@Override
	public boolean isResultReadyToBeHandled()
	{
		if(!HostSpecifier.isValid(tf_hostname.getValue()))
		{
			GeneralDialogs.error("Invalid hostname", "Must be: hostname, ipv4 or ipv6.");
			return false;
		}
		
		if((tf_port.getConvertedValue() == null) || ((Integer) tf_port.getConvertedValue() < 0)) 
		{
			GeneralDialogs.error("Invalid port", "Must be a non-negative integer.");
			return false;
		}
		return true;
	}

	@Override
	public void addArgs(List<Object> arguments)
	{
		arguments.add(tf_hostname.getValue());
		arguments.add(tf_port.getConvertedValue());
	}
}