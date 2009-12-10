package de.otype.android.defac.datacollect.interfaces;

import android.app.Activity;
import android.content.BroadcastReceiver;

public interface IBatteryStatus {
	/**
	 * Get the current battery capacity via BroadCastReceiver
	 * @return battery capacity via BroadCastReceiver
	 */
	public BroadcastReceiver getCapacity(Activity act);
	
	/**
	 * Get the current capacity of the battery
	 * @return Capacity level of battery
	 */
	public int getCapacity();
		
}
