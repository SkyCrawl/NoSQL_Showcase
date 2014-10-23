package redis;

import java.util.Collection;

import com.basho.riak.client.IRiakObject;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class RedisTableRow implements Item
{
	private final IRiakObject value;
	
	public RedisTableRow(IRiakObject value)
	{
		super();
		this.value = value;
	}

	// -----------------------------------------------------------------------------
	// METHODS REQUIRED BY TABLE

	@Override
	public Property<String> getItemProperty(Object id)
	{
		if(id == RedisContainer.column_Key)
		{
			return new RedisProperty(value.getKey());
		}
		else
		{
			return new RedisProperty(value.getValueAsString());
		}
	}

	@Override
	public Collection<?> getItemPropertyIds()
	{
		return RedisContainer.columnHeaders;
	}
	
	// -----------------------------------------------------------------------------
	// OPTIONAL METHODS

	@Override
	public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException
	{
		return false;
	}
	
	@Override
	public boolean removeItemProperty(Object id) throws UnsupportedOperationException
	{
		return false;
	}
}
