package de.otype.android.defac.datacollect.implementation;

import java.io.File;
import java.io.IOException;

import android.util.Log;

public class FileLister {
	private static final String TAG = "FileLister";
	
	public static void listFiles(String root) {
        try {
			new SearchFileRecursive() {
				public void onFile(final File f) {
					Log.i(TAG, f.toString());
				}
			}.traverse(new File(root));
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}    	
    }
}
