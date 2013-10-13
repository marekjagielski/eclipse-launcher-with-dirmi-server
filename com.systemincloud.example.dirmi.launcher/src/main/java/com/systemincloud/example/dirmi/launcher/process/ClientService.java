package com.systemincloud.example.dirmi.launcher.process;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientService extends Remote {
	
	public final String CLIENT_SERVICE_NAME = "client-service-name";
	
	String hello(String name) throws RemoteException;
}
