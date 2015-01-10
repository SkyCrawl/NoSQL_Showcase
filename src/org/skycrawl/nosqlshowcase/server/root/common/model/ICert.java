package org.skycrawl.nosqlshowcase.server.root.common.model;

public interface ICert
{
	int getVersion();
	void setVersion(int version);
	String getPubKeyAlg();
	void setPubKeyAlg(String pubKeyAlg);
	String getOrganizationName();
	void setOrganizationName(String organizationName);
	String getOrganizationUnit();
	void setOrganizationUnit(String organizationUnit);
	String getCommonName();
	void setCommonName(String commonName);
}