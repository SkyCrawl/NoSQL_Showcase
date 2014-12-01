package org.skycrawl.nosqlshowcase.server.riak.model;

public class RiakX509Cert extends AbstractRiakSingleLinkValue
{
	public int version;
	public String pubKeyAlg;
	public String organizationName;
	public String organizationUnit;
	public String commonName;
	
	@Override
	public String toKey()
	{
		return String.valueOf(hashCode());
	}
	
	public int getVersion()
	{
		return this.version;
	}
	public void setVersion(int version)
	{
		this.version = version;
	}
	public String getPubKeyAlg()
	{
		return this.pubKeyAlg;
	}
	public void setPubKeyAlg(String pubKeyAlg)
	{
		this.pubKeyAlg = pubKeyAlg;
	}
	public String getOrganizationName()
	{
		return this.organizationName;
	}
	public void setOrganizationName(String organizationName)
	{
		this.organizationName = organizationName;
	}
	public String getOrganizationUnit()
	{
		return this.organizationUnit;
	}
	public void setOrganizationUnit(String organizationUnit)
	{
		this.organizationUnit = organizationUnit;
	}
	public String getCommonName()
	{
		return this.commonName;
	}
	public void setCommonName(String commonName)
	{
		this.commonName = commonName;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.commonName == null) ? 0 : this.commonName.hashCode());
		result = prime
				* result
				+ ((this.organizationName == null) ? 0 : this.organizationName
						.hashCode());
		result = prime
				* result
				+ ((this.organizationUnit == null) ? 0 : this.organizationUnit
						.hashCode());
		result = prime * result
				+ ((this.pubKeyAlg == null) ? 0 : this.pubKeyAlg.hashCode());
		result = prime * result + this.version;
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RiakX509Cert other = (RiakX509Cert) obj;
		if (this.commonName == null)
		{
			if (other.commonName != null)
				return false;
		}
		else if (!this.commonName.equals(other.commonName))
			return false;
		if (this.organizationName == null)
		{
			if (other.organizationName != null)
				return false;
		}
		else if (!this.organizationName.equals(other.organizationName))
			return false;
		if (this.organizationUnit == null)
		{
			if (other.organizationUnit != null)
				return false;
		}
		else if (!this.organizationUnit.equals(other.organizationUnit))
			return false;
		if (this.pubKeyAlg == null)
		{
			if (other.pubKeyAlg != null)
				return false;
		}
		else if (!this.pubKeyAlg.equals(other.pubKeyAlg))
			return false;
		if (this.version != other.version)
			return false;
		return true;
	}
}