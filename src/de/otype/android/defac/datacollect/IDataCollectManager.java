/**
 * 
 */
package de.otype.android.defac.datacollect;

import android.app.Activity;

/**
 * @author hgschmidt
 *
 */
public interface IDataCollectManager {

	/**
	 * Collect data and send it via REST Query to webservice
	 * @param optionals Set this to true if optional data shall also be collected
	 * @param pollingIntervalls Seconds between two collection runs
	 * @return Success status
	 */
	public boolean sendDataToRestService(boolean optionals, int pollingIntervalls);
	
	/**
	 * Run all data collect modules and see output in adb log
	 * @param act Current activity
	 */
	public void testModules(Activity act);
}
