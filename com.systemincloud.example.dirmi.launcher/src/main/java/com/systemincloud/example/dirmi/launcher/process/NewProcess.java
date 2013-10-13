package com.systemincloud.example.dirmi.launcher.process;

import com.systemincloud.example.dirmi.launcher.dirmi.DirmiClient;

public class NewProcess {

	public static void main(String[] args) {
		DirmiClient.INSTANCE.register(ClientService.CLIENT_SERVICE_NAME, new ClientService() {
			@Override
			public String hello(String name) {
				return "Hello " + name;
			}
		});
		
		DirmiClient.INSTANCE.registrationDone();
	}
}
