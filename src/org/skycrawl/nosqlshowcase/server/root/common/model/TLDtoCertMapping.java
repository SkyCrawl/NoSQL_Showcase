package org.skycrawl.nosqlshowcase.server.root.common.model;

import org.skycrawl.nosqlshowcase.server.root.ui.venndiagram.VennSet;
import org.skycrawl.nosqlshowcase.server.root.util.Tuple;

public class TLDtoCertMapping extends Tuple<VennSet, VennSet>
{
	public TLDtoCertMapping(VennSet value1, VennSet value2)
	{
		super(value1, value2);
	}
	
	public boolean isValid()
	{
		return (getValue1() != null) && (getValue2() != null) && !getValue1().equals(getValue2());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!isValid())
		{
			throw new IllegalStateException("Underlying pair of venn sets is not valid.");
		}
		else
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			
			TLDtoCertMapping other = (TLDtoCertMapping) obj;
			if(!other.isValid())
			{
				throw new IllegalStateException("The argument key does not contain a valid pair of venn sets.");
			}
			else
			{
				if (!getValue1().equals(other.getValue1()) && !getValue1().equals(other.getValue2())) 
				{
					return false;
				}
				if (!getValue2().equals(other.getValue1()) && !getValue2().equals(other.getValue2())) 
				{
					return false;
				}
				return true;
			}
		}
	}
}