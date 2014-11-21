package org.skycrawl.nosqlshowcase.riak;

import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class RiakProperty implements Property<String>
{
	private final String value;
	
	public RiakProperty(String value)
	{
		this.value = value;
	}

	@Override
	public String getValue()
	{
		return value;
	}
	
	// ---------------------------------------------------------------------------------
	// OPTIONAL:

	@Override
	public void setValue(String newValue) throws com.vaadin.data.Property.ReadOnlyException
	{
	}

	@Override
	public Class<? extends String> getType()
	{
		return String.class;
	}

	@Override
	public boolean isReadOnly()
	{
		return true;
	}

	@Override
	public void setReadOnly(boolean newStatus)
	{
	}
}
