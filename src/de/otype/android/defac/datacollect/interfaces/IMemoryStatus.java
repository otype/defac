package de.otype.android.defac.datacollect.interfaces;


public interface IMemoryStatus {
	
	/**
	 * @return Total memory available for application
	 */
	public long getTotalMemoryForApp();
	
	/**
	 * @return Used memory of application
	 */
	public long getUsedMemoryForApp();
	
	/** 
	 * @return Available memory for application
	 */
	public long getAvailMemoryForApp();
	
	/**
	 * @return Get size of total memory of system
	 */
	public int getMemTotal();
	
	/**
	 * @return Get size of free memory of system
	 */
	public int getMemFree();

	/**
	 * @return Get size of memory held in buffers
	 */	
	public int getMemBuffers();
	
	/**
	 * @return
	 */	
	public int getMemCached();
		
	/**
	 * @return Get size of memory in swap cached
	 */	
	public int getMemSwapCached();

	/**
	 * @return Get size of active memory
	 */	
	public int getMemActive();

	/**
	 * @return Get size of inactive memory
	 */	
	public int getMemInactive();
	
	/**
	 * @return Get size of total swap
	 */	
	public int getMemSwapTotal();

	/**
	 * @return Get size of free swap
	 */	
	public int getMemSwapFree();

	/**
	 * @return Get size of dirty memory
	 */	
	public int getMemDirty();
	
	/**
	 * @return Get size of write back memory
	 */	
	public int getMemWriteBack();


}
