package org.skycrawl.nosqlshowcase.server.root.common.db;

import java.util.List;

public interface IVersionsSpecifier
{
	String getConciseString();
	List<String> getAllVersions();
}