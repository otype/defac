package de.otype.android.defac.datacollect.implementation;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;

import android.util.Log;
import de.otype.android.defac.datacollect.interfaces.IFileIndex;

public class FileIndex implements IFileIndex {

	/* Android-specific TAG for log messages */
	private static final String TAG = "FileIndex";

	/* List of all found entries in given directories */
	private LinkedList<String> finalList = new LinkedList<String>();

	/* Filenamefilter (not really required in Indexer */
	private FilenameFilter filter;
	
	/* Recursive search: yes or no? */
	private boolean recurse;
	
	/**
	 * The constructor creates a filter, sets the boolean to "true" and calls
	 * countMedia() for "/system", "/data" and "/sdcard".
	 */
	public FileIndex() {
		recurse = true;		
		filter = new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return true;
			}
		};		
	}


	/**
	 * Get all files from default directories /sdcard and /data
	 * @return List of all files in given default directories
	 */
	public LinkedList<String> getFiles() {
		String[] str = {"/sdcard", "/data" };		
		return getFiles(str);
	}
	
	/**
	 * Get all files from given directories
	 * @param directories Array of directories to be scanned
	 * @return List of all files in given directories
	 */
	public LinkedList<String> getFiles(String[] directories) {
		for (String directory : directories) {
			finalList.addAll(this.listFiles(new File(directory), filter, recurse));
		}		
		return finalList;
	}
	
	/**
	 * This method runs recursively through the structure of the file system.
	 * 
	 * @return string of files
	 */	
	private LinkedList<String> listFiles(File directory, FilenameFilter filter,
			boolean recurse) {

		LinkedList<String> list = new LinkedList<String>();
		int hash;
		long fileSize;

		// Get files / directories in the directory
		File[] entries = directory.listFiles();

		// Go over entries
		for (File entry : entries) {
			Log.d(TAG, "Entries: " + entries.length + " Entry: "
					+ entry.getAbsolutePath());

			// If there is no filter or the filter accepts the
			// file / directory, add it to the list
			if (filter == null || filter.accept(directory, entry.getName())) {
				fileSize = entry.length();
				hash = entry.hashCode();

				Log.d(TAG, "Entry: " + entry.getAbsolutePath() + " Size: "
						+ fileSize + " Hash: " + hash);

				list.add(entry.getName() + ":");
				list.add("" + hash + ":");
				list.add("" + fileSize + ":");
			}

			// If the file is a directory and the recurse flag
			// is set, recurse into the directory
			if (recurse && entry.isDirectory()) {
				Log.d(TAG, entry + " is a directory");
				list.addAll(listFiles(entry, filter, recurse));
			}

		}
		return list;
	}

}