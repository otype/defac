package de.otype.android.defac.datacollect.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import de.otype.android.defac.datacollect.interfaces.IBatteryStatus;

public class BatteryStatus implements IBatteryStatus {

	public String ModuleName;
	private static final String TAG = "BatteryStatus";
	private int level = -1;
	
	public BroadcastReceiver getCapacity(Activity act) {		
        act.registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));        
        return batteryReceiver;        
	}

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive( Context context, Intent intent )
        {
            level = intent.getIntExtra("level", 0);
            Log.i("BatteryStatus", "Level = " + level);
        }
    };
    
    /**
     * 	
     */
    public int getCapacity() {
    	BufferedReader bfr;
		FileInputStream fin;
		String battery_capacity = "/sys/devices/platform/rs30100001:00000000/power_supply/battery/capacity";
		
		try
		{
			fin = new FileInputStream (new File(battery_capacity));
		    bfr = new BufferedReader(new InputStreamReader(fin), 8);
		    String line;
		    while ((line = bfr.readLine()) != null) {
		    	level = Integer.parseInt(line);
		    }
		    bfr.close();
		} catch (FileNotFoundException fnfe) {
			Log.e(TAG, "File " + battery_capacity + " was not found!");
		} catch (IOException e) {
			Log.e(TAG, "Unable to read from file");
		}
		
    	return level;
    }
}
