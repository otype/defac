package de.otype.android.defac.datacollect.interfaces;

import java.util.LinkedList;

public interface IFileIndex {

	/**
	 * Get all files from default directories /sdcard and /data
	 * @return List of all files in given default directories
	 */
	public LinkedList<String> getFiles();

	/**
	 * Get all files from given directories
	 * @param directories Array of directories to be scanned
	 * @return List of all files in given directories
	 */
	public LinkedList<String> getFiles(String[] directories);

}
