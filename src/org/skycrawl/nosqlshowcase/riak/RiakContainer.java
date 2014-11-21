package org.skycrawl.nosqlshowcase.riak;

import java.util.Collection;
import java.util.HashSet;

import org.skycrawl.nosqlshowcase.ui.IMyContainer;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class RiakContainer implements Container, IMyContainer
{
	private final RiakConnection riakDB;
	private String currentBucketID;
	
	public static final String column_Key = "Key";
	public static final String column_Value = "Value";
	
	public static final Collection<String> columnHeaders = new HashSet<String>();
	static
	{
		columnHeaders.add(column_Key);
		columnHeaders.add(column_Value);
	}
	
	public RiakContainer(RiakConnection riakDB)
	{
		this.riakDB = riakDB;
		this.currentBucketID = null;
	}
	
	@Override
	public boolean setContext(Object newContext)
	{
		String previousBucketID = this.currentBucketID;
		this.currentBucketID = (String) newContext;
		return previousBucketID != this.currentBucketID;
	}
	
	// -----------------------------------------------------------------------------
	// METHODS REQUIRED BY TABLE

	@Override
	public Collection<?> getContainerPropertyIds()
	{
		return columnHeaders;
	}
	
	@Override
	public Class<?> getType(Object propertyId)
	{
		return String.class;
	}
	
	@Override
	public Collection<?> getItemIds()
	{
		return riakDB.getKeysInBucket(currentBucketID);
	}
	
	@Override
	public int size()
	{
		return getItemIds().size();
	}
	
	@Override
	public RiakTableRow getItem(Object itemId)
	{
		return new RiakTableRow(riakDB.getValue(currentBucketID, (String) itemId));
	}
	
	@Override
	public Property<String> getContainerProperty(Object itemId, Object propertyId)
	{
		return getItem(itemId).getItemProperty(propertyId);
	}

	@Override
	public boolean containsId(Object itemId)
	{
		return getItem(itemId) != null;
	}

	// -----------------------------------------------------------------------------
	// OPTIONAL METHODS
	
	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException
	{
		return false;
	}
	
	@Override
	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException
	{
		return false;
	}
	
	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object addItem() throws UnsupportedOperationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeItem(Object itemId) throws UnsupportedOperationException
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean removeAllItems() throws UnsupportedOperationException
	{
		// TODO Auto-generated method stub
		return false;
	}
}