package org.skycrawl.nosqlshowcase.server.root.util;

import java.util.List;

public interface IVersionsSpecifier
{
	String getConciseString();
	List<String> getAllVersions();
}