/**
 * 
 */
package de.otype.android.defac.datacollect.interfaces;

/**
 * @author hgschmidt
 *
 */
public interface IProcessStatus {
	
	
	/**
	 * @return Array of strings for each column of "ps" of
	 * given process 
	 */
	public String[] getProcessStatusOf(String processName);
	
	/**
	 * @return Get the username of the given process
	 */
	public String getUserNameOf(String processName);

	/**
	 * @return Get the process ID of the given process
	 */
	public String getPIDOf(String processName);
	
	/**
	 * @return Get the parent process ID the given process 
	 */
	public String getPPIDOf(String processName);
	
	/**
	 * Get VSIZE of given process
	 * @return VSIZE of process in kbytes
	 */
	public String getMemoryStatusOf(String processName);

	/**
	 * @return Get the resident size in memory of the given process
	 */
	public String getRSSOf(String processName);

	/**
	 * @return Get the WCHAN of the given process
	 */
	public String getWCHANOf(String processName);
	
	/**
	 * @return Get the PC of the given process
	 */
	public String getPCOf(String processName);
	
	
}
