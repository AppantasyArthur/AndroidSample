package com.appantasy.androidapptemplate;

import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;

public class DeviceCollector implements RegistryListener {

	@Override
	public void afterShutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeShutdown(Registry arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void localDeviceAdded(Registry registry, LocalDevice device) {
		
		String deviceType = device.getType().toString();
		System.out.println("from local : " + deviceType);

	}

	@Override
	public void localDeviceRemoved(Registry arg0, LocalDevice arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
		
		//RemoteDevice retDevice = null;
		String deviceType = device.getType().toString();
		System.out.println("from remote" + deviceType);
		if(deviceType.equals("")){
		
			MediaServerDevice msDevice = (MediaServerDevice) device;
			
			
		} else if(deviceType.equals("")){
			
			MediaRenderDevice mrDevice = (MediaRenderDevice) device;
			
		} else if(deviceType.equals("")){
			
			MediaManagerDevice mmDevice = (MediaManagerDevice) device;
			
			
		} 

	}

	@Override
	public void remoteDeviceDiscoveryFailed(Registry arg0, RemoteDevice arg1,
			Exception arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remoteDeviceDiscoveryStarted(Registry arg0, RemoteDevice arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remoteDeviceRemoved(Registry arg0, RemoteDevice arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remoteDeviceUpdated(Registry arg0, RemoteDevice arg1) {
		// TODO Auto-generated method stub

	}

}
