package com.example.nosql_homework;

import java.io.File;

import com.vaadin.data.Container;
import com.vaadin.server.FileResource;

public class DatabaseInfo implements IConnectionSetup
{
	public final FileResource imgResource;
	
	public String hostname;
	public int port;
	
	public final MyDBConnection connection;
	public final Container dataSource;
	public final IMyUI customDBApplicationUI;
	
	public DatabaseInfo(String imgName, String defaultHostname, int defaultPort, MyDBConnection connection, Container tableContainer, IMyUI customDBApplicationUI)
	{
		this.imgResource = new FileResource(new File(ServerConfig.imagesPath + imgName));
		
		this.hostname = defaultHostname;
		this.port = defaultPort;
		
		this.connection = connection;
		this.dataSource = tableContainer;
		this.customDBApplicationUI = customDBApplicationUI;
	}

	@Override
	public boolean connect(String currentHostname, int currentPort) throws Exception
	{
		if(connection.connect(currentHostname, currentPort))
		{
			customDBApplicationUI.newConnectionEstablished();
			return true;
		}
		else
		{
			return false;
		}
	}
}
