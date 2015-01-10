package org.skycrawl.nosqlshowcase.server.root.common.sample;

import org.skycrawl.nosqlshowcase.server.root.common.model.ICert;

public class DefaultCertObject implements ICert
{
	public int version;
	public String pubKeyAlg;
	public String organizationName;
	public String organizationUnit;
	public String commonName;
	
	@Override
	public int getVersion()
	{
		return this.version;
	}
	@Override
	public void setVersion(int version)
	{
		this.version = version;
	}
	@Override
	public String getPubKeyAlg()
	{
		return this.pubKeyAlg;
	}
	@Override
	public void setPubKeyAlg(String pubKeyAlg)
	{
		this.pubKeyAlg = pubKeyAlg;
	}
	@Override
	public String getOrganizationName()
	{
		return this.organizationName;
	}
	@Override
	public void setOrganizationName(String organizationName)
	{
		this.organizationName = organizationName;
	}
	@Override
	public String getOrganizationUnit()
	{
		return this.organizationUnit;
	}
	@Override
	public void setOrganizationUnit(String organizationUnit)
	{
		this.organizationUnit = organizationUnit;
	}
	@Override
	public String getCommonName()
	{
		return this.commonName;
	}
	@Override
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
		DefaultCertObject other = (DefaultCertObject) obj;
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