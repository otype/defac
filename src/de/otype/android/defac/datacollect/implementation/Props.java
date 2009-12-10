/**
 * 
 */
package de.otype.android.defac.datacollect.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import de.otype.android.defac.datacollect.interfaces.IProps;

/**
 * @author hgschmidt
 *
 */
public class Props implements IProps {

	private static final String TAG = "PropertyStatus";
	private HashMap<String,String> properties;
	private Context context;
	
	public Props() {
		properties = new HashMap<String, String>();
		this.initPropertiesHash();
	}
	
	public Props(Context ctx) {
		properties = new HashMap<String, String>();
		this.initPropertiesHash();
		this.context = ctx;
	}
	
	/* (non-Javadoc)
	 * @see de.dailab.smartmobile.datacollect.IProperties#getProp(java.lang.String)
	 */
	public String getProperty(String propertyName) {
		return properties.get(propertyName);
	}
	
	public String getPropertySoundsLike(String similarToPropertyName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getAllProperties() {		
	    String line = "";
	    for(String key : properties.keySet()) {
	    	line += "KEY : " + key + " => VALUE : " + properties.get(key) + "\n";
	    }
		return line;
	}
	
	public String getIMEI() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		return tm.getDeviceId().toString();
	}

	public String getIMSI() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		return tm.getSimSerialNumber().toString();
	}
	
	/* ================================================
	 * PRIVATE
	 * ================================================ */
	private void initPropertiesHash() {
		Runtime rt = Runtime.getRuntime();
		String line;		
		try {
			Process p = rt.exec("/system/bin/getprop");
			BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()), 8);
			while ((line = bfr.readLine()) != null) {
				String l[] = line.split("]:");
				String key = (l[0]).trim().replace("[", "");
				String value = (l[1]).trim().replace("[", "").replace("]", "");
				properties.put(key, value);
				Log.d(TAG, "KEY = " + key + " VALUE = " + value);
			}			
		} catch (IOException e) {			
			Log.d(TAG, e.getMessage());
		}				
	}
}
