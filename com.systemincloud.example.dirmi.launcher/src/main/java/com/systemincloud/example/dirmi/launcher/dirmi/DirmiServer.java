package com.systemincloud.example.dirmi.launcher.dirmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.cojen.dirmi.Environment;
import org.cojen.dirmi.Session;
import org.cojen.dirmi.SessionAcceptor;
import org.cojen.dirmi.SessionListener;

public enum DirmiServer {
    INSTANCE;
    
    public static final int PORT = 1099;
    
    private Environment env;
    private Map<String, Remote> remotes = new HashMap<String, Remote>();
    
	public void start(final RegistrationListener registrationListener) {
		try {
			env = new Environment();
			final Root root = new Root() {
				@Override public void registerRemote(String name, Remote object) {
					remotes.put(name, object);
				}
				@Override
				public void registrationDone() throws RemoteException {
					registrationListener.registrationDone();
				}
			};
			final SessionAcceptor sessionAcceptor = env.newSessionAcceptor(PORT);
			sessionAcceptor.accept(new SessionListener() {
				@Override
				public void established(Session session) throws IOException {
					sessionAcceptor.accept(this);
					session.setClassLoader(getClass().getClassLoader());
					session.send(root);
				}
				
				@Override
				public void establishFailed(IOException exception) throws IOException {
					sessionAcceptor.accept(this);
				}
				
				@Override
				public void acceptFailed(IOException exception) throws IOException {
					
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
