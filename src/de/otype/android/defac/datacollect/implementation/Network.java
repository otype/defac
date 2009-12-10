package de.otype.android.defac.datacollect.implementation;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings.SettingNotFoundException;
import de.otype.android.defac.datacollect.interfaces.INetwork;
import de.otype.android.defac.datacollect.interfaces.IProps;

public class Network implements INetwork {
	private ContentResolver cr;
	
	public Network(Context ctx){
		cr = ctx.getContentResolver();
	}
	
	public int getWifiStatus() {
		int wifiStatus;
		try {
			wifiStatus = android.provider.Settings.Secure.getInt(cr, android.provider.Settings.Secure.WIFI_ON);
			return wifiStatus;
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public int getBluetoothStatus() {
		
		int bluetoothStatus;
		try {
			bluetoothStatus = android.provider.Settings.Secure.getInt(cr, android.provider.Settings.Secure.BLUETOOTH_ON);
			return bluetoothStatus;
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public String getNetworkType() {
		IProps prop = new Props();
		String networkType = prop.getProperty("gsm.network.type");
		return networkType;
	}
}
