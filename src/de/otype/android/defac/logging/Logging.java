package de.otype.android.defac.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;
import de.otype.android.defac.datacollect.helpers.Helpers;

public class Logging {
	private static final String TAG = "DEFAC[Logging]";
	private static final String SERVICE_RESTART_LOGFILE = "/sdcard/def/restart.service.log";

	/**
	 * Add time stamp to restart log
	 */
	public static void addTimeToRestartLog() {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(SERVICE_RESTART_LOGFILE, true));
			bw.write(Helpers.getDateTime());
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			Log.e(TAG, ioe.getMessage());
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
					Log.e(TAG, ioe2.getMessage());
				}
		}
	}

}
