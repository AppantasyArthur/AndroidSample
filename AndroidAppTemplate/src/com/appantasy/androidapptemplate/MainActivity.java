package com.appantasy.androidapptemplate;

import java.util.Map;
import java.util.logging.Logger;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.android.AndroidUpnpServiceImpl;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.controlpoint.SubscriptionCallback;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.gena.CancelReason;
import org.teleal.cling.model.gena.GENASubscription;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.ActionArgument;
import org.teleal.cling.model.meta.ActionArgument.Direction;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.state.StateVariableValue;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class MainActivity extends ListActivity {
	
	private static Logger log = Logger.getLogger(MainActivity.class.getName());
	
	// PROPERTIES
	
	private ArrayAdapter<DeviceDisplay> listAdapter;

    private AndroidUpnpService upnpService; // android's service
    
    private RegistryListener registryListener = new BrowseRegistryListener();
    private ServiceConnection serviceConnection = new ServiceConnection() {

    	@Override
        public void onServiceConnected(ComponentName className, IBinder service) { // 
            
    		upnpService = (AndroidUpnpService) service; // 一個協定、interface
            
            //UpnpService us = upnpService.get(); // 真正存取 upnp 的 service，非 Android Service

            // Refresh the list with all known devices
            listAdapter.clear();
            for (Device device : upnpService.getRegistry().getDevices()) {
                ((BrowseRegistryListener) registryListener).deviceAdded(device); // 加到 ListView
            }

            // Getting ready for future device advertisements
            upnpService.getRegistry().addListener(registryListener); // 呼叫 Service 裡的 Registry，加入 Listener (from Activity)

            // Search asynchronously for all devices
            upnpService.getControlPoint().search(); // 呼叫 Service 裡的 upnpservice 開始 search
        }

    	@Override
        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }

    };
   
    // METHODS
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

        listAdapter =
            new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1
            );
        
        //listAdapter.
        setListAdapter(listAdapter);

        // 綁定 service
        getApplicationContext().bindService(
            new Intent(this, AndroidUpnpServiceImpl.class), // intent service :  AndroidUpnpServiceImpl，android service
            serviceConnection, // action after connect/disconnect service
            Context.BIND_AUTO_CREATE
        );
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        getApplicationContext().unbindService(serviceConnection);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, 0, 0, R.string.search_lan)
	        .setIcon(android.R.drawable.ic_menu_search);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == 0 && upnpService != null) {
	        upnpService.getRegistry().removeAllRemoteDevices();
	        upnpService.getControlPoint().search();
	    }
	    return false;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		DeviceDisplay dd = (DeviceDisplay)getListView().getItemAtPosition(position);
		Device d = dd.getDevice();
		log.info("click " + d.getType().getType());
		
		// Monitor
		Service serviceSwitchPower = null;
		Service[] deviceServices = d.getServices();
		for(int i = 0;i < deviceServices.length;i++){
			if(deviceServices[i].getServiceType().toString().endsWith("urn:schemas-upnp-org:service:SwitchPower:1")){
				serviceSwitchPower = deviceServices[i];
			}
		}
		SwitchSubscriptionCallback sc = new SwitchSubscriptionCallback(serviceSwitchPower == null ? deviceServices[0] : serviceSwitchPower);
		upnpService.getControlPoint().execute(sc);
		
		// Invoke
		Action getTarget = serviceSwitchPower.getAction("GetTarget");
		
		/*Action setTarget = serviceSwitchPower.getAction("SetTarget");
		ActionArgumentValue[] values = new ActionArgumentValue[1];
		values[0] = new ActionArgumentValue(new ActionArgument("NewTargetValue", "Target", Direction.IN), true);*/
		
		ActionInvocation ai = new ActionInvocation(getTarget);
		
		//ActionInvocation ai = new ActionInvocation(setTarget, values);
		
		SwitchActionCallback sac = new SwitchActionCallback(ai);
		upnpService.getControlPoint().execute(sac);
		
		super.onListItemClick(l, v, position, id);
		
	}
	
	class SwitchActionCallback extends ActionCallback{

		protected SwitchActionCallback(ActionInvocation actionInvocation) {
			super(actionInvocation);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void failure(ActionInvocation arg0, UpnpResponse arg1,
				String arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void success(ActionInvocation invocation) {
		
			log.info("Action : " + invocation.getAction().getName() + ", Value : " + invocation.getOutput());
			
		}
		
	}

	class SwitchSubscriptionCallback extends SubscriptionCallback{

		protected SwitchSubscriptionCallback(Service service) {
			super(service);
			
		}

		@Override
		protected void ended(GENASubscription arg0, CancelReason arg1,
				UpnpResponse arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void established(GENASubscription arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void eventReceived(GENASubscription subscription) {
			
			for (Map.Entry<String, StateVariableValue> entry : ((Map<String, StateVariableValue>) subscription.getCurrentValues()).entrySet()) {
            
				log.info("switch event : " + entry.getKey() + ", value : " + entry.getValue());
				
			}
			
		}

		@Override
		protected void eventsMissed(GENASubscription arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void failed(GENASubscription arg0, UpnpResponse arg1,
				Exception arg2, String arg3) {
			// TODO Auto-generated method stub
			
		}
		
	}


	class BrowseRegistryListener extends DefaultRegistryListener {

		@Override
	    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
	        //deviceAdded(device);
	    }

	    @Override
	    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
	        runOnUiThread(new Runnable() {
	            public void run() {
	                /*Toast.makeText(
	                        BrowseActivity.this,
	                        "Discovery failed of '" + device.getDisplayString() + "': " +
	                                (ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
	                        Toast.LENGTH_LONG
	                ).show();*/
	            }
	        });
	        deviceRemoved(device);
	    }

	    @Override
	    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
	        deviceAdded(device);
	        System.out.println("remote found : " + device.getType().getDisplayString());
	    }

	    @Override
	    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
	        deviceRemoved(device);
	    }

	    @Override
	    public void localDeviceAdded(Registry registry, LocalDevice device) {
	        //deviceAdded(device);
	    	System.out.println("local found : " + device.getType().getDisplayString());
	    }

	    @Override
	    public void localDeviceRemoved(Registry registry, LocalDevice device) {
	        deviceRemoved(device);
	    }

	    public void deviceAdded(final Device device) {
	        runOnUiThread(new Runnable() {
	            public void run() {
	                DeviceDisplay d = new DeviceDisplay(device);
	                int position = listAdapter.getPosition(d);
	                if (position >= 0) {
	                    // Device already in the list, re-set new value at same position
	                    listAdapter.remove(d);
	                    listAdapter.insert(d, position);
	                } else {
	                    listAdapter.add(d);
	                }
	            }
	        });
	    }

	    public void deviceRemoved(final Device device) {
	        runOnUiThread(new Runnable() {
	            public void run() {
	                listAdapter.remove(new DeviceDisplay(device));
	            }
	        });
	    }
		
	}
	
}
