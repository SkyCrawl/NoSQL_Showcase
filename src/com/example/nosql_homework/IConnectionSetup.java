package com.example.nosql_homework;

public interface IConnectionSetup
{
	boolean connect(String currentHostname, int currentPort) throws Exception;
}
