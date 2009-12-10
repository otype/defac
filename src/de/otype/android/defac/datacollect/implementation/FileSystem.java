package de.otype.android.defac.datacollect.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.util.Log;
import de.otype.android.defac.datacollect.interfaces.IFileSystem;


public class FileSystem implements IFileSystem {
	private final static String TAG = "FileSystem";
	
	/**
	 * This class provides methods to get information about the filesystems.
	 * @return Number of mounted filesystems
	 */	
	public int getNumberOfFileSystems() {
		/**
		 * This method executes the shell command "mount" in the runtime environment to get the number of existing filesystems. 
		 * @return an integer containing the number of existing filesystems
		 */
		int fileSystemsNo = 0;
		String[] cmd_elements = {"mount"};
		try {
			Process prcs = Runtime.getRuntime().exec(cmd_elements);
			BufferedReader bfr = new BufferedReader(new InputStreamReader(prcs.getInputStream()), 8);
			while (bfr.readLine() != null){
				fileSystemsNo++;
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}		
		return fileSystemsNo;		
	}

	/**
	 * This method executes the shell command "df" in the runtime environment to get size-information about the existing filesystems.
	 * @return a HashMap with the full path name as the key, and an array of size-information [total size] [occupied space] [available space] as the corresponding value.
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, int[]> getSizeInformation() {
		//Array of [total size] [occupied space] [available space]
		int[] sizeInformation = new int[3];
		HashMap<String, int[]> map = new HashMap();
		String line = null;
		
		String[] cmd_elements = {"df"};
		try {
			Process prcs = Runtime.getRuntime().exec(cmd_elements);
			BufferedReader bfr = new BufferedReader(new InputStreamReader(prcs.getInputStream()), 8);
			String[] lineArr = new String[2];
			String[] lineArr2 = new String[6];
			
			
			while ((line = bfr.readLine()) != null){
				lineArr = line.split(":");
				lineArr2 = lineArr[1].split(" ");
				
				sizeInformation[0] = new  Integer(lineArr2[1].replace("K", "")).intValue();
				sizeInformation[1] = new  Integer(lineArr2[3].replace("K", "")).intValue();
				sizeInformation[2] = new  Integer(lineArr2[5].replace("K", "")).intValue();
				
				map.put(lineArr[0], sizeInformation);
				}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}		
		return map;		
	}
	
}
