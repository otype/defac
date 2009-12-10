/**
 * 
 */
package de.otype.android.defac.datacollect.implementation;

import de.otype.android.defac.datacollect.skelettons.DataCollectModule;

/**
 * @author hgschmidt
 *
 */
public class BatteryStat extends DataCollectModule {

	private String ModuleName = "Battery Status";
	private String DBModuleName = "BATTERYSTATUS";
	
	@Override
	public String[] getModuleMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModuleName() {		
		return ModuleName;
	}

}
