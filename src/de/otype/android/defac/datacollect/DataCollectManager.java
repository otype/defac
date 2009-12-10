package de.otype.android.defac.datacollect;

import android.app.Activity;
import android.util.Log;
import de.otype.android.defac.datacollect.implementation.BatteryStatus;
import de.otype.android.defac.datacollect.implementation.FileLister;
import de.otype.android.defac.datacollect.implementation.MemoryStatus;
import de.otype.android.defac.datacollect.implementation.Network;
import de.otype.android.defac.datacollect.implementation.ProcessStatus;
import de.otype.android.defac.datacollect.interfaces.IBatteryStatus;
import de.otype.android.defac.datacollect.interfaces.IMemoryStatus;
import de.otype.android.defac.datacollect.interfaces.INetwork;
import de.otype.android.defac.datacollect.interfaces.IProcessStatus;
import de.otype.android.defac.datacollect.interfaces.IProps;

public class DataCollectManager implements IDataCollectManager {
	private static final String TAG = "Overview";
	private Activity activity;

	public DataCollectManager() {	
	}
	
	public DataCollectManager(Activity act) {
		this.activity = act;
	}	
	
	
	public boolean sendDataToRestService(boolean optionals, int pollingIntervalls) {
		
//		QueryRESTService qrs = new QueryRESTService(0, 'p', getUID());
		
		return true;
	} 
		


	/* =====================================================
	 * (non-Javadoc)
	 * @see de.dailab.smartmobile.datacollect.IDataCollectManager#testModules(android.app.Activity)
	 * TESTING
	 * ===================================================== */
	
	public void testModules(Activity act) {
		Log.i(TAG, "====================================================");

		String process = "/system/bin/servicemanager";
		IProcessStatus idc = new ProcessStatus();
		Log.i(TAG, "Process = " + process + ", USER = " + idc.getUserNameOf(process));
		Log.i(TAG, "Process = " + process + ", PID = " + idc.getPIDOf(process));
		Log.i(TAG, "Process = " + process + ", PPID = " + idc.getPPIDOf(process));
		Log.i(TAG, "Process = " + process + ", VSIZE = " + idc.getMemoryStatusOf(process));
		Log.i(TAG, "Process = " + process + ", RSS = " + idc.getRSSOf(process));
		Log.i(TAG, "Process = " + process + ", WCHAN = " + idc.getWCHANOf(process));
		Log.i(TAG, "Process = " + process + ", PC = " + idc.getPCOf(process));

		IBatteryStatus ibc = new BatteryStatus();
		Log.i(TAG, "Battery Level = " + ibc.getCapacity(act));
		Log.i(TAG, "Battery Level 2 = " + ibc.getCapacity());

		FileLister.listFiles("/system/bin");

		IMemoryStatus ims = new MemoryStatus();
		Log.i(TAG, "Total Memory = " + ims.getTotalMemoryForApp());
		Log.i(TAG, "Available Memory = " + ims.getAvailMemoryForApp());
		Log.i(TAG, "Used Memory = " + ims.getUsedMemoryForApp());
		Log.i(TAG, "Proc Total Memory = " + ims.getMemTotal());
		Log.i(TAG, "Proc Free Memory = " + ims.getMemFree());
		Log.i(TAG, "Proc Buffers Memory = " + ims.getMemBuffers());
		Log.i(TAG, "Proc Cached Memory = " + ims.getMemCached());
		Log.i(TAG, "Proc Swap Cached Memory = " + ims.getMemSwapCached());
		Log.i(TAG, "Proc Active Memory = " + ims.getMemActive());
		Log.i(TAG, "Proc Inactive Memory = " + ims.getMemInactive());
		Log.i(TAG, "Proc SwapTotal Memory = " + ims.getMemSwapTotal());
		Log.i(TAG, "Proc SwapFree Memory = " + ims.getMemSwapFree());
		Log.i(TAG, "Proc Dirty Memory = " + ims.getMemDirty());
		Log.i(TAG, "Proc WriteBack Memory = " + ims.getMemWriteBack());

		IProps iprop =  new de.otype.android.defac.datacollect.implementation.Props(act.getBaseContext());
		Log.i(TAG, "Prop [ARGH] = " + iprop.getProperty("ARGH"));
		Log.i(TAG, "All Prop -> " + iprop.getAllProperties());
		Log.i(TAG, "IMEI = " + iprop.getIMEI());
		Log.i(TAG, "IMSI =  " + iprop.getIMSI());
		Log.i(TAG, "====================================================");

		INetwork test = new Network(act);
		Log.i("xxxxxxxxxxxxxx", "" + test.getBluetoothStatus());
		Log.i("xxxxxxxxxxxxxx", "" + test.getWifiStatus());
		Log.i("xxxxxxxxxxxxxx", test.getNetworkType());      
	}	
	
}
