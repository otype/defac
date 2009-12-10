/**
 * 
 */
package de.otype.android.defac.datacollect.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.otype.android.defac.datacollect.helpers.Helpers;
import de.otype.android.defac.datacollect.interfaces.IProcessStatus;
import android.util.Log;

/**
 * @author hgschmidt
 *
 */
public class ProcessStatus implements IProcessStatus {
	private static final String TAG = "IProcessStatus";


	/**
	 * 
	 */
	public String[] getProcessStatusOf(String processName) {		
		Runtime rt = Runtime.getRuntime();
		String line = null;
		Boolean found = false;
		try {
			Process p = rt.exec("/system/bin/ps");
			BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()), 8);
			while ((line = bfr.readLine()) != null) {								
				if (line.contains(processName)) {
					found = true;
					break;
				}
			}			
		} catch (IOException e) {			
			Log.d(TAG, e.getMessage());
		}				
				
		String retval = "NONE";
		if (found) retval = Helpers.trimmer(line);		
		return retval.split(":");
	}
	
	/**
	 * 
	 */
	public String getUserNameOf(String processName) {		
		return getProcessStatusOf(processName)[0];
	}

	/**
	 * 
	 */
	public String getPIDOf(String processName) {		
		return getProcessStatusOf(processName)[1];
	}
	
	/**
	 * 
	 */
	public String getPPIDOf(String processName) {		
		return getProcessStatusOf(processName)[2];
	}
	
	/**
	 * Get VSIZE of given process
	 * @return VSIZE of process in kbytes
	 */
	public String getMemoryStatusOf(String processName) {		
		return getProcessStatusOf(processName)[3];
	}

	/**
	 * 
	 */
	public String getRSSOf(String processName) {		
		return getProcessStatusOf(processName)[4];
	}

	/**
	 * 
	 */
	public String getWCHANOf(String processName) {		
		return getProcessStatusOf(processName)[5];
	}
	
	/**
	 * 
	 */
	public String getPCOf(String processName) {		
		return getProcessStatusOf(processName)[6];
	}

	
}
