<b>HOW TO RUN EXAMPLE:</b>
 * Download eclipse kepler from http://www.eclipse.org/downloads/ (preferred ... for JEE Devolppers because it has already m2e)
 * Run eclipse and import projects *.libs, *.parent, *.launcher
 * Build with maven separately project *.libs, next *.launcher
 * Click right on project launcher and go to Debug as -> Eclipse Application
 * In new eclipse instance go to menu Run -> Run Configurations ..., click right on "Dirmi test" -> "New ..." and then click Run on bottom of new page

<b>DESCRIPTION OF EXAMPLE:</b>

 1) com.systemincloud.example.dirmi.libs
 
 This is pom-first maven pde project that contains libraries: dirmi-1.0 and cojen-2.2 from https://github.com/cojen.
 
 2) com.systemincloud.example.dirmi.launcher

This is manifest-first pde maven project that demonstrate how to implement own launcher in eclipse and establish inter-process communication between eclipse plugin and launched application. IPC is based on library dirmi (https://github.com/cojen).
