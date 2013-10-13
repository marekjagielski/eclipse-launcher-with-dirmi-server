package com.systemincloud.example.dirmi.launcher;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.osgi.framework.Bundle;

import com.systemincloud.example.dirmi.launcher.dirmi.DirmiServer;
import com.systemincloud.example.dirmi.launcher.dirmi.RegistrationListener;
import com.systemincloud.example.dirmi.launcher.process.ClientService;
import com.systemincloud.example.dirmi.launcher.process.NewProcess;

public class ProcessLaunchDelegate extends JavaLaunchDelegate implements RegistrationListener {

	public static final String LAUNCHER_PLUGIN_ID  = "com.systemincloud.example.dirmi.launcher";
	public static final String LIBS_PLUGIN_ID      = "com.systemincloud.example.dirmi.libs";

    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        if(!ILaunchManager.RUN_MODE.equals(mode)) return;
        if (monitor == null) monitor = new NullProgressMonitor();

        DirmiServer.INSTANCE.start(this);
        
        super.launch(configuration, mode, launch, monitor);
        
        while(!launch.isTerminated()) {
            try { Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }
                
        DirmiServer.INSTANCE.stop();
    }

	@Override
	public void registrationDone() {
		System.out.println("Registration done");
        ClientService cs = DirmiServer.INSTANCE.getRemote(ClientService.CLIENT_SERVICE_NAME, ClientService.class);
        try {
			System.out.println(cs.hello("Marek"));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
    
    public String getMainTypeName(ILaunchConfiguration configuration) throws CoreException {
        return NewProcess.class.getName();
    }

    @Override
    public String[] getClasspath(ILaunchConfiguration configuration) throws CoreException {
        List<String> extendedClasspath = new ArrayList<String>();
        Collections.addAll(extendedClasspath, super.getClasspath(configuration));
        if (Platform.inDevelopmentMode()) {
            try {
                extendedClasspath.add(pluginIdToJarPath(LAUNCHER_PLUGIN_ID) + "target/classes");
            } catch (IOException e) { throw new CoreException(new Status(IStatus.ERROR, LAUNCHER_PLUGIN_ID, IStatus.OK, "Failed to compose classpath!", e)); }
        }
        try {
            extendedClasspath.add(pluginIdToJarPath(LAUNCHER_PLUGIN_ID));
            Bundle bundle = LauncherPlugin.getDefault().getBundle(LIBS_PLUGIN_ID);
            if (bundle != null) {
                Enumeration<?> jarEnum = bundle.findEntries("/", "*.jar", true);
                while (jarEnum != null && jarEnum.hasMoreElements()) {
                    URL element = (URL) jarEnum.nextElement();
                    extendedClasspath.add(FileLocator.toFileURL(element).getFile());
                }
            }
        } catch (IOException e) { throw new CoreException(new Status(IStatus.ERROR, LAUNCHER_PLUGIN_ID, IStatus.OK, "Failed to compose classpath!", e)); }
        return extendedClasspath.toArray(new String[extendedClasspath.size()]);
    }

    public static String pluginIdToJarPath(String pluginId) throws IOException {
        Bundle bundle = LauncherPlugin.getDefault().getBundle(pluginId);
        URL url = bundle.getEntry("/");
        if (url == null) throw new IOException();
        return FileLocator.toFileURL(url).getFile();
    }

}
