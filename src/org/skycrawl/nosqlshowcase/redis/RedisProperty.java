package org.skycrawl.nosqlshowcase.redis;

import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class RedisProperty implements Property<String>
{
	private final String value;
	
	public RedisProperty(String value)
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
