package com.example.nosql_homework;

public abstract class MyDBConnection implements IConnectionSetup
{
	private String currentHostname;
	private int currentPort;
	
	public MyDBConnection()
	{
		this.currentHostname = null;
		this.currentPort = -1;
	}
	
	public boolean connect(String currentHostname, int currentPort) throws Exception
	{
		if((this.currentHostname != currentHostname) || (this.currentPort != currentPort))
		{
			connectPrivate(currentHostname, currentPort);
			this.currentHostname = currentHostname;
			this.currentPort = currentPort;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected abstract void connectPrivate(String currentHostname, int currentPort) throws Exception;
	protected abstract void destroyCurrentSession();
}
