package com.systemincloud.example.dirmi.launcher.dirmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Root extends Remote {
	void registerRemote(String name, Remote object) throws RemoteException;
	void registrationDone() throws RemoteException;
}
