package de.otype.android.defac.datacollect.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import de.otype.android.defac.datacollect.interfaces.IMemoryStatus;

import android.util.Log;

public class MemoryStatus implements IMemoryStatus {
	private final static String TAG = "MemoryStatus";
	HashMap<String, Integer> procMemoryInfo;
	
	public MemoryStatus() {
		procMemoryInfo = readMemInfoFromProc();
	}
	
	public long getAvailMemoryForApp() {
		return (int)Runtime.getRuntime().freeMemory();				
	}

	public long getTotalMemoryForApp() {		
		return (int)Runtime.getRuntime().totalMemory();
	}

	public long getUsedMemoryForApp() {
		return (int)Runtime.getRuntime().totalMemory() - (int)Runtime.getRuntime().freeMemory();
	}
		
	public int getMemTotal() {		
		return (int)procMemoryInfo.get("MEMTOTAL");
	}
	
	public int getMemFree() {
		return (int)procMemoryInfo.get("MEMFREE");
	}
	
	public int getMemBuffers() {
		return (int)procMemoryInfo.get("BUFFERS");	
	}
	
	public int getMemCached() {
		return (int)procMemoryInfo.get("CACHED");
	}
	
	public int getMemSwapCached() {
		return (int)procMemoryInfo.get("SWAPCACHED");
	}

	public int getMemActive() {
		return (int)procMemoryInfo.get("ACTIVE");
	}

	public int getMemInactive() {
		return (int)procMemoryInfo.get("INACTIVE");
	}
	
	public int getMemSwapTotal() {
		return (int)procMemoryInfo.get("SWAPTOTAL");
	}

	public int getMemSwapFree() {
		return (int)procMemoryInfo.get("SWAPFREE");
	}

	public int getMemDirty() {
		return (int)procMemoryInfo.get("DIRTY");
	}
	
	public int getMemWriteBack() {
		return (int)procMemoryInfo.get("WRITEBACK");
	}

	
	
	
	/* ===================================================
	 * PRIVATE
	 * =================================================== */
	
	private HashMap<String,Integer> readMemInfoFromProc() {
    	BufferedReader bfr;
		FileInputStream fin;
		String memoryInfo = "/proc/meminfo";
		HashMap<String,Integer> mhash = new HashMap<String,Integer>();
		
		try
		{
			fin = new FileInputStream (new File(memoryInfo));
		    bfr = new BufferedReader(new InputStreamReader(fin), 8);
		    String line;		    
		    while ((line = bfr.readLine()) != null) {
		    	String arr[] = line.split(":");		    	
		    	String arrr[] = (arr[1].trim()).split(" ");
		    	mhash.put(arr[0].toUpperCase(), Integer.parseInt(arrr[0]));
		    	Log.d(TAG, "KEY = " + arr[0] + " VALUE = " + mhash.get(arr[0]));
		    }
		    bfr.close();
		} catch (FileNotFoundException fnfe) {
			Log.e(TAG, "File " + memoryInfo + " was not found!");
		} catch (IOException e) {
			Log.e(TAG, "Unable to read from file");
		}
		
    	return mhash;
    }	
}
