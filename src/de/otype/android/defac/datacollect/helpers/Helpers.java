/**
 * 
 */
package de.otype.android.defac.datacollect.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import android.util.Log;

/**
 * @author hgschmidt
 * 
 */
public class Helpers {

	private static String TAG = "DEFAC[Helpers]";

	/**
	 * Freaking Java trim won't work!!! This is an own implementation of trim.
	 * 
	 * @return String with all fields separated by ":"
	 */
	public static String trimmer(String line) {
		String retval = "";
		Boolean series = false;

		for (Character a : line.toCharArray()) {
			if (!a.equals(' ') && !series)
				retval += a;

			if (!a.equals(' ') && series) {
				retval += ":" + a;
				series = false;
			}

			if (a.equals(' ')) {
				series = true;
			}
		}

		return retval;
	}

	/**
	 * Search for directories and files with a given root and filter.
	 * 
	 * @param directory
	 *            Sets the path to the root-directory.
	 * @param filter
	 *            Sets the filter.
	 * @param recurse
	 *            Set true for searching recursively.
	 * @return Collection of found files
	 */
	public static ArrayList<File> listFiles(File directory,
			FilenameFilter filter, boolean recurse) {
		ArrayList<File> files = new ArrayList<File>();

		File[] entries = directory.listFiles();

		for (File entry : entries) {
			// If there is no filter or the filter accepts the
			// file / directory, add it to the list
			if (filter == null || filter.accept(directory, entry.getName())) {
				files.add(entry);
			}

			// If the file is a directory and the recurse flag
			// is set, recurse into the directory
			if (recurse && entry.isDirectory()) {
				files.addAll(listFiles(entry, filter, recurse));
			}
		}

		// Return collection of files
		return files;
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static long getFileSize(String filename) {
		File file = new File(filename);
		if (!file.exists() || !file.isFile()) {
			Log.e(TAG, "GETFILESIZE: File \"" + filename + "\" doesn\'t exist");
			return -1;
		}
		return (file.length() / 1024);
	}

	/**
	 * 
	 * @param aFile
	 * @return
	 */
	public static String getLastLines(String filename, int number) {
		File aFile = new File(filename);
		StringBuilder contents = new StringBuilder();
		LinkedList<String> ll = new LinkedList<String>();

		try {
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					ll.add(line);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			Log.e(TAG, ex.getMessage());
		}

		if ((ll.size() - number) <= 0) {
			Log.e(TAG, "Requested number of lines exceeds lines of file");
			return "Requested number of lines exceeds lines of file";
		}

		for (int i = (ll.size() - 1); i >= (ll.size() - number); i--) {
			contents.append(ll.get(i - 1));			
			contents.append("\n");
		}
				
		return contents.toString();
	}

	/**
	 * 
	 * @return
	 */
	public static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

//	/**
//	 * 
//	 * @param strDate
//	 * @return
//	 */
//	public static Date getDateFromString(String strDate) {
//		strDate = Helpers.trimmer(strDate);
//		
//		return null;
//	}
	
	/**
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String getDateDifference(Date d1, Date d2) {
		String unit = "seconds";
		long diff = d2.getTime() - d1.getTime();
		diff = diff / 1000;
		
		// /* More than 7 days? Then go to weeks */
		// if (diff >= (60 * 60 * 24 * 7)) {
		// diff = diff / 7;
		// unit = "weeks";
		// }

		/* More than 72 hours? Then go to days */
		if (diff >= (60 * 60 * 24 * 3)) {
			diff = diff / 24;
			unit = "days";
		}

		/* More than 60 minutes? Then go to hours */
		if (diff >= (60 * 60) && diff < (60 * 60 * 24 * 3)) {
			diff = diff / 60;
			unit = "hours";
		}

		/* More than 60 seconds? Then go to minutes */
		if (diff >= 60 && diff < (60 * 60)) {
			diff = diff / 60;
			unit = "minutes";
		}

		return diff + " " + unit;
	}
}
