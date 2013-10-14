<b>HOW TO RUN EXAMPLE:</b>
 * Download eclipse kepler from http://www.eclipse.org/downloads/ (preferred ... for JEE Devolppers because it has already m2e)
 * Run eclipse and import projects *.libs, *.parent, *.launcher
 * Build with maven separately project *.libs, next *.launcher
 * Click right on project launcher and go to Debug as -> Eclipse Application
 * In new eclipse instance create any project
 * Then go to menu Run -> Run Configurations ..., click right on "Dirmi test" -> "New ..." and then click Run on bottom of new page

<b>DESCRIPTION OF EXAMPLE:</b>

 1) com.systemincloud.example.dirmi.libs
 
 This is pom-first maven pde project that contains libraries: dirmi-1.0 and cojen-2.2 from https://github.com/cojen.
 
 2) com.systemincloud.example.dirmi.launcher

This is manifest-first pde maven project that demonstrate how to implement own launcher in eclipse and establish inter-process communication between eclipse plugin and launched application. IPC is based on library dirmi (https://github.com/cojen).


Description of project:
 * <b>ProcessLaunchDelegate :</b> Class is defined in extension point in plugin.xml. It starts Dirmi server and run new java process. Just before starting, it sets class path of the new process to two eclipse plugins.
When the registration of the new process's remote services is finished ProcessLaunchDelegate run a  remote ClientService.
 * <b>DirmiServer :</b> When start() DirmiServer set new session listener on port 1099 with a Root  remote instance. Root remote instance register all remotes received from client (new process). When it receives a signal that registration is finish it notifies ProcessLaunchDelegate.
 * <b>NewProcess :</b> It is a standalone java application that is run by ProcessLaunchDelegate. It registers a ClientService instance in remote Root service on eclipse.
 * <b>ClientService :</b> Simple remote service that returns a modified String.
