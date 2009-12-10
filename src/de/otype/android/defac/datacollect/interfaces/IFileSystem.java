/**
 * 
 */
package de.otype.android.defac.datacollect.interfaces;

import java.util.HashMap;

/**
 * @author ajage
 *
 */
public interface IFileSystem {
	
	/**
	 * Get current number of mounted filesystems
	 * @return Number of mounted filesystems
	 */
	public int getNumberOfFileSystems();
	
	/**
	 * This method executes the shell command "df" in the runtime environment to get size-information about the existing filesystems.
	 * @return a HashMap with the full path name as the key, and an array of size-information [total size] [occupied space] [available space] as the corresponding value.
	 */
	public HashMap<String, int[]> getSizeInformation();
}
