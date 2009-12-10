package de.otype.android.defac.datacollect.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import de.otype.android.defac.logging.Logging;

public class RubyService extends Service {

	private static final String TAG 			= "DEFAC[RubyService]";
	public static boolean serviceIsRunning 		= false;
	public static int timeIntervall				= 45;

	private String rubyExecutable;
	private String defExecutable;
	private ControlLoopRunnable clr;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "RubyService initialized!");
		serviceIsRunning = true;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		defExecutable = intent.getStringExtra("RubyScript");
		rubyExecutable = intent.getStringExtra("PathToRuby");
		startRubyCollector();
		startControlLoop();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "Destroying Ruby Collector Service");
		stopService();
		super.onDestroy();
	}		
	
	/**
	 * Stops the service
	 */
	public void stopService(){
		Log.i(TAG, "Stopping Ruby Collector Service");
		serviceIsRunning = false;
		try {
			clr.stop();
		} catch (NullPointerException npe) {
			Log.e(TAG, "Uh-oh! Collector Thread does not exist anymore!");
		}
		stopRubyCollector();
		stopSelf();
	}

	/**
	 * Starts the Ruby Collector Daemon
	 */
	public void startRubyCollector() {		
		Log.i(TAG, "Starting Ruby Collector { " + rubyExecutable + " " + defExecutable + " start }");
		try {			
			/* Go ahead and start the daemon */
			Runtime rt = Runtime.getRuntime(); 
			String cmd = rubyExecutable + " " + defExecutable + " start";
			Process prcs = rt.exec(cmd);
			
			/* Put a time stamp into logs */
			Logging.addTimeToRestartLog();
		} catch (IOException ioe) {
			Log.e(TAG, ioe.getMessage());
		}
	}

	/**
	 * Stops the Ruby Collector Daemon
	 */
	public void stopRubyCollector() {					
		Log.i(TAG, "Stopping Ruby Collector { " + rubyExecutable + " " + defExecutable + " stop }");
		try {
			Runtime rt = Runtime.getRuntime(); 
			String cmd = rubyExecutable + " " + defExecutable + " stop";
			Process prcs = rt.exec(cmd);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}	
	
	/**
	 * The control loop thread which checks every XX seconds
	 * if the ruby collector daemon is still running
	 */
	public void startControlLoop() {
		Log.d(TAG, "Starting new Control Loop thread");
		clr = new ControlLoopRunnable();
		new Thread(clr).start();
	}
	
	/**
	 * Control Loop Thread which checks if the Ruby Collector daemon is still
	 * active and running. If not, it waits a short period and then tries
	 * to restart the daemon. 
	 */
	private class ControlLoopRunnable implements Runnable {		
		public void run() {
			Log.d(TAG, "New Control Loop thread established");
			while (serviceIsRunning) {				
				if (!RubyService.isRunning(rubyExecutable)) {					
					Log.d(TAG, "Ruby Collector is not running anymore ... restarting daemon!");										
					try {
						Log.d(TAG, "Waiting 10sec before restarting ...");
						Thread.sleep(10000);						
						
						Log.i(TAG, "Starting Ruby Collector { " + rubyExecutable + " " + defExecutable + " start }");
						Runtime rt = Runtime.getRuntime(); 
						String cmd = rubyExecutable + " " + defExecutable + " start";
						Process prcs = rt.exec(cmd);
					} catch (InterruptedException ie) {
						Log.e(TAG, ie.getMessage());
					} catch (IOException ioe) {
						Log.e(TAG, ioe.getMessage());
					}
				}				

				Log.d(TAG, "Status OK! Control Loop Thread going back to sleep! Time interval set to " + timeIntervall + " seconds!");
				try {
					Thread.sleep(timeIntervall * 1000);
				} catch (InterruptedException ie) {
					Log.e(TAG, ie.getMessage());
				}						
				Log.d(TAG, "Woke up! Starting checks now!");
			}
		}

		public void stop() {
			Log.i(TAG, "Stopping Control Loop Thread");
		}		
	}
	
	/**
	 * Check if Ruby process is still running
	 * @param processName
	 * @return boolean
	 */
	public static boolean isRunning(String processName) {		
		Runtime rt = Runtime.getRuntime();
		String line = null;		

		try {
			Process p = rt.exec("/system/bin/ps");
			BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()), 8);
			while ((line = bfr.readLine()) != null) {								
				if (line.contains(processName)) {					
					return true;
				}
			}			
		} catch (IOException e) {			
			Log.d(TAG, e.getMessage());
		}				
		return false;
	}

}
