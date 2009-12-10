package de.otype.android.defac.datacollect;

import java.util.LinkedList;

import de.otype.android.defac.datacollect.implementation.BatteryStatus;
import de.otype.android.defac.datacollect.implementation.FileIndex;
import de.otype.android.defac.datacollect.implementation.FileSystem;
import de.otype.android.defac.datacollect.implementation.MediaFind;
import de.otype.android.defac.datacollect.implementation.MemoryStatus;
import de.otype.android.defac.datacollect.implementation.ProcessStatus;
import de.otype.android.defac.datacollect.implementation.Props;
import de.otype.android.defac.datacollect.interfaces.IBatteryStatus;
import de.otype.android.defac.datacollect.interfaces.IFileIndex;
import de.otype.android.defac.datacollect.interfaces.IFileSystem;
import de.otype.android.defac.datacollect.interfaces.IMediaFind;
import de.otype.android.defac.datacollect.interfaces.IMemoryStatus;
import de.otype.android.defac.datacollect.interfaces.IProcessStatus;
import de.otype.android.defac.datacollect.interfaces.IProps;

public class DataSets {

	/* List of datasets */
	public LinkedList<String> setList;
	
	/**
	 * Default constructor
	 */
	public DataSets() {
		setList = new LinkedList<String>();
	}
	
	/**
	 * Dataset linked list as a string
	 * @return Dataset list as one single string
	 */
	public String toString() {
		String all = "";
		for (String s : setList) {
			all += s;
		}
		return all;
	}
	
	/*
	 * ALL POSSIBLE DATA SETS
	 * (a few are just examples, e.g. addPropsSet ... more can be collected)
	 */
	
	/**
	 * Add Battery Capacity features set
	 */
	public void addBatteryCapacitySet() {
		IBatteryStatus ibs = new BatteryStatus();
		setList.add("&BATT_CAPACITY=" + ibs.getCapacity());		
	}
	
	/**
	 * Add Filesystem set
	 */
	public void addFileSystemSet() {
		IFileSystem ifs = new FileSystem();
		setList.add("&NUM_OF_FILESYSTEM=" + ifs.getNumberOfFileSystems());
		int count = 0;
		
		for (String fs : ifs.getSizeInformation().keySet()) {
			count++;
			setList.add("&FILESYSTEM_" + count + "=" + fs);
			setList.add("&FILESYSTEM_" + count + "_TOTAL=" + ifs.getSizeInformation().get(fs)[0]);
			setList.add("&FILESYSTEM_" + count + "_USED=" + ifs.getSizeInformation().get(fs)[1]);
			setList.add("&FILESYSTEM_" + count + "_AVAIL=" + ifs.getSizeInformation().get(fs)[2]);
		}
	}
	
	/**
	 * Add FileIndexer file set
	 */
	public void addFileIndexerSet() {
		IFileIndex ifi = new FileIndex();
		
		String[] arr = { "/sdcard", "/data", "/system" };
		setList.addAll(ifi.getFiles(arr));
	}
	
	/**
	 * Add media file set
	 */
	public void addMediaFindSet() {
		IMediaFind imf = new MediaFind();
		setList.add("&FILES_MUSIC_NO=" + imf.getMusicNo());
		setList.add("&FILES_VIDEO_NO=" + imf.getVidNo());
		setList.add("&FILES_PICT_NO=" + imf.getPicNo());
	}
	
	public void addMemoryStatusSet() {
		IMemoryStatus ims = new MemoryStatus();
		setList.add("&MEMORY_AVAIL_FOR_APP=" + ims.getAvailMemoryForApp());
		setList.add("&MEMORY_TOTAL_FOR_APP=" + ims.getTotalMemoryForApp());
		setList.add("&MEMORY_USED_FOR_APP=" + ims.getUsedMemoryForApp());
		setList.add("&MEMORY_ACTIVE=" + ims.getMemActive());
		setList.add("&MEMORY_BUFFERS=" + ims.getMemBuffers());
		setList.add("&MEMORY_CACHED=" + ims.getMemCached());
		setList.add("&MEMORY_DIRTY=" + ims.getMemDirty());
		setList.add("&MEMORY_FREE=" + ims.getMemFree());
		setList.add("&MEMORY_INACTIVE=" + ims.getMemInactive());
		setList.add("&MEMORY_SWAP_CACHED=" + ims.getMemSwapCached());
		setList.add("&MEMORY_SWAP_FREE=" + ims.getMemSwapFree());
		setList.add("&MEMORY_SWAP_TOTAL=" + ims.getMemSwapTotal());
		setList.add("&MEMORY_TOTAL=" + ims.getMemTotal());
		setList.add("&MEMORY_WRITE_BACK=" + ims.getMemWriteBack());		
	}
	
	public void addProcessStatusSet(String processName) {
		IProcessStatus ips = new ProcessStatus();
		setList.add("&PROCESS_MEMORY_STATUS=" + ips.getMemoryStatusOf(processName));
		setList.add("&PROCESS_PC=" + ips.getPCOf(processName));
		setList.add("&PROCESS_PID=" + ips.getPIDOf(processName));
		setList.add("&PROCESS_PPID=" + ips.getPPIDOf(processName));
		setList.add("&PROCESS_RSS=" + ips.getRSSOf(processName));
		setList.add("&PROCESS_USERNAME=" + ips.getUserNameOf(processName));
		setList.add("&PROCESS_WCHAN=" + ips.getWCHANOf(processName));		
	}
	
	public void addPropsSet() {
		IProps ip = new Props();
		setList.add("&PROP_GSM_OPERATOR_ALPHA=" + ip.getProperty("gsm.operator.alpha"));
	}
}
