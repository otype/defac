package de.otype.android.defac.datacollect.implementation;

import java.io.File;
import java.io.FilenameFilter;

import de.otype.android.defac.datacollect.interfaces.IMediaFind;

public class MediaFind implements IMediaFind{
	/**
	 * The constructor creates a filter, sets the boolean to "true" and calls countMedia() for "/system", "/data" and "/sdcard".
	 */
	public MediaFind(){
		FilenameFilter filter = new FilenameFilter() {
			
			public boolean accept(File dir, String filename) {
				return true;			       
			     	}
		}; 
		boolean recurse = true;
		
        this.countMedia(new File("/system"), filter, recurse);
        this.countMedia(new File("/data"), filter, recurse);
        this.countMedia(new File("/sdcard"), filter, recurse);
	}
	
	/** 
	 * This array contains the number of media files: [pictures] - [music] - [videos].
	 */
	private int[] mediaNo = {0,0,0};
	
	/**
	 * This method runs recursively through the structure of the filesystem. 
	 * @return number of media
	 */
	private int[] countMedia(
			File directory,
			FilenameFilter filter,
			boolean recurse)
	{
		
		
		// Get files / directories in the directory
		File[] entries = directory.listFiles(); 
		
				// Go over entries
		for (File entry : entries)
		{
			
						// If there is no filter or the filter accepts the 
			// file / directory, add it to the list
			
				
			if (filter == null || filter.accept(directory, entry.getName()))
			{
				//Check if file is picture
				if ((entry.getName().toLowerCase().endsWith("jpg"))   || 
		 			(entry.getName().toLowerCase().endsWith("jpeg")) ||
		 			(entry.getName().toLowerCase().endsWith("png"))  ||
		 			(entry.getName().toLowerCase().endsWith("gif"))  || 
		 			(entry.getName().toLowerCase().endsWith("bmp"))) {
						this.mediaNo[0]++;
		 			}
				
				//Check if file is music
				if ((entry.getName().toLowerCase().endsWith("mp3"))   || 
		 			(entry.getName().toLowerCase().endsWith("wma"))	  ||
			 		(entry.getName().toLowerCase().endsWith("ogg"))) {
						this.mediaNo[1]++;						
		 			}
				
				//Check if file is video
				if ((entry.getName().toLowerCase().endsWith("mpeg"))   || 
					(entry.getName().toLowerCase().endsWith("avi"))) {
							this.mediaNo[2]++;
			 			}
				
				
			}
			
			// If the file is a directory and the recurse flag
			// is set, recurse into the directory
			if (recurse && entry.isDirectory())
			{
				countMedia(entry, filter, recurse);
			}
		}
		
		// Return number of media files
		return mediaNo;		
	}
	
	public int[] getMediaNo(){
		return this.mediaNo;
	}
	/**
	 * 
	 * @return number of pictures
	 */
	public int getPicNo(){
		return this.mediaNo[0];
	}
	/**
	 * 
	 * @return number of music files
	 */
	public int getMusicNo(){
		return this.mediaNo[1];
	}
	/**
	 * 
	 * @return number of videos
	 */
	public int getVidNo(){
		return this.mediaNo[2];
	}
}
