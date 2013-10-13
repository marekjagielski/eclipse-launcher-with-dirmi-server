package com.systemincloud.example.dirmi.launcher.dirmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cojen.dirmi.Environment;
import org.cojen.dirmi.Session;

public enum DirmiClient {
    
	INSTANCE;
	
	private Environment env = new Environment();
	private Root root;

	DirmiClient() {
		try {
			Session rootSession = env.newSessionConnector("localhost", DirmiServer.PORT).connect();
			root = (Root) rootSession.receive();
		} catch (IOException e) { }
	}
	
	public void register(String name, Remote object) {
		try {
			root.registerRemote(name, object);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void registrationDone() {
		try {
			root.registrationDone();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}