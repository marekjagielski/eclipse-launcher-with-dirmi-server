package com.systemincloud.example.dirmi.launcher.dirmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.cojen.dirmi.Environment;

public enum DirmiServer {
    INSTANCE;
    
    public static final int PORT = 1099;
    
    private Environment env;
    private Map<String, Remote> remotes = new HashMap<String, Remote>();
    
	public void start(final RegistrationListener registrationListener) {
		try {
			env = new Environment();
			env.newSessionAcceptor(PORT).acceptAll(new Root() {
				@Override public void registerRemote(String name, Remote object) {
					remotes.put(name, object);
				}
				@Override
				public void registrationDone() throws RemoteException {
					registrationListener.registrationDone();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getRemote(String name, Class<T> clazz) {
		return (T) remotes.get(name);
	}
	
	public void stop() {
		try {
			env.close();
		} catch (IOException e) { }
		remotes = new HashMap<String, Remote>();
	}
}
