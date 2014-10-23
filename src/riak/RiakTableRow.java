package riak;

import java.util.Collection;

import com.basho.riak.client.IRiakObject;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class RiakTableRow implements Item
{
	private final IRiakObject value;
	
	public RiakTableRow(IRiakObject value)
	{
		super();
		this.value = value;
	}

	// -----------------------------------------------------------------------------
	// METHODS REQUIRED BY TABLE

	@Override
	public Property<String> getItemProperty(Object id)
	{
		if(id == RiakContainer.column_Key)
		{
			return new RiakProperty(value.getKey());
		}
		else
		{
			return new RiakProperty(value.getValueAsString());
		}
	}

	@Override
	public Collection<?> getItemPropertyIds()
	{
		return RiakContainer.columnHeaders;
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
