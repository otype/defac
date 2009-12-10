package de.otype.android.defac;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.otype.android.defac.datacollect.helpers.Helpers;
import de.otype.android.defac.datacollect.services.RubyService;

public class DefAC extends Activity {
	
	public final static String TAG		= "DEFAC";	
	public final static String VERSION	= "v0.1";
	public final String SERVICE_NAME	= "RubyService";
	
	private final String PATHTORUBY 	= "/data/tools/ruby1.9.1";
	private final String LOGFILE		= "/sdcard/def/def.log";
	private final String RESTARTLOGFILE	= "/sdcard/def/restart.log";
	private final String SERVICE_RESTART_LOGFILE = "/sdcard/def/restart.service.log";
	private final String RBSCRIPTS	 	= "rbscripts.zip";
	private final String RBSCRIPT 		= "main.rb";
	private final int numberOfRestarts	= 1;

	private final int MENU_START_SERVICE 	= 0;
	private final int MENU_STOP_SERVICE 	= 1;
	private final int MENU_REFRESH			= 2;
	
	private boolean status_ok 			= true;
	
	private TextView tvLogfileSize;	
	private TextView tvLastNumRestarts;
	private TextView tvLastRestarts;
	private TextView tvCurrentDate;
	private TextView tvServiceState;
	private TextView tvDefStatus;
	private TextView tvDefRCState;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* Unpack RBSCRIPTS zip file */
		if(!getFileStreamPath(RBSCRIPTS).exists()) {
			unzippingRubyScriptsFromRawResource();
		}			
	}
	
	@Override
	public void onStart() {
		super.onStart();
		setContentView(R.layout.main);
		_initializeMainScreen();
		_updateMainScreen();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if(!checkServiceStatus(SERVICE_NAME, getRunningServices())){
			menu.add(Menu.NONE, MENU_START_SERVICE, 0, "Start " + SERVICE_NAME);
		} else {
			menu.add(Menu.NONE, MENU_STOP_SERVICE, 0, "Stop " + SERVICE_NAME);
		}
		menu.add(Menu.NONE, MENU_REFRESH, 0, "Refresh");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent iRubyStart = new Intent(this, RubyService.class);
		switch (item.getItemId()) {
		case MENU_START_SERVICE:
			if(status_ok) {
				String name = "RubyScript";
				iRubyStart.putExtra(name, getFileStreamPath(RBSCRIPTS).getParent()+ "/" + RBSCRIPT);
				iRubyStart.putExtra("PathToRuby", PATHTORUBY);
				startService(iRubyStart);
				_updateMainScreen();
				return true;
			} else {
				AlertDialog.Builder missing = new AlertDialog.Builder(this);
				missing.setMessage("Either ruby-interpreter or ruby-script is missing!");
				missing.setCancelable(true);
				missing.setNegativeButton("Back", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				missing.show();
				_updateMainScreen();
				return false;
			}
		case MENU_STOP_SERVICE:
			stopService(iRubyStart);
			_updateMainScreen();
			return true;
		case MENU_REFRESH:
			_updateMainScreen();
			return true;
		}  
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	/* *************************************************
	 * Android-additionals
	 */
	
	/**
	 * 
	 */
	public List<ActivityManager.RunningServiceInfo> getRunningServices(){
		ActivityManager rubyStartManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE); 
		return rubyStartManager.getRunningServices(1000);
	}

	/**
	 * 
	 * @param serviceName
	 * @param services
	 * @return
	 */
	public boolean checkServiceStatus(String serviceName, List<ActivityManager.RunningServiceInfo> services){
		boolean running = false;
		runthrough: for(ActivityManager.RunningServiceInfo serviceManager : services){
			if(serviceManager.service.toString().contains(serviceName)){
				running = true;
				break runthrough;
			}
		} 
		return running;
	}

	/**
	 * 
	 * @param services
	 */
	public void outputRunningServices(List<ActivityManager.RunningServiceInfo> services) {
		for(ActivityManager.RunningServiceInfo serviceManager : services){
			Log.i(TAG, serviceManager.service.toString());
		}
	}

	/* *************************************************
	 * non-android stuff
	 */
	
	private void _initializeMainScreen() {
		tvDefStatus = (TextView) findViewById(R.id.defstatus);
		tvServiceState = (TextView) findViewById(R.id.serviceState);
		tvDefRCState = (TextView) findViewById(R.id.defrcState);
		tvLogfileSize = (TextView) findViewById(R.id.logfileSize);
		tvCurrentDate = (TextView) findViewById(R.id.currentDate);
		tvLastNumRestarts = (TextView) findViewById(R.id.lastNumRestarts);
		tvLastRestarts = (TextView) findViewById(R.id.lastRestarts);
	}
	
	private void _updateMainScreen() {
		tvDefStatus.setText("DEFAC " + VERSION);
		if(checkServiceStatus(SERVICE_NAME, getRunningServices())) {
			tvServiceState.setText("RUNNING");
		} else {
			tvServiceState.setText("DOWN");
		}
		
		if (RubyService.isRunning(PATHTORUBY)) {
			tvDefRCState.setText("RUNNING");
		} else {
			tvDefRCState.setText("DOWN");
		}
		
		tvLogfileSize.setText(Helpers.getFileSize(LOGFILE) + " Kbytes");		
		tvCurrentDate.setText(Helpers.getDateTime());
		tvLastNumRestarts.setText("Last " + numberOfRestarts + " restarts");
		tvLastRestarts.setText(Helpers.getLastLines(SERVICE_RESTART_LOGFILE, numberOfRestarts));

	}
	

	/**
	 * 
	 */
	public void unzippingRubyScriptsFromRawResource() {
		InputStream brRubyScripts = getResources().openRawResource(R.raw.rbscripts);
		try {
			FileOutputStream oswRubyScript = openFileOutput(RBSCRIPTS, 0);
			byte[] buff = new byte[1024];

			int length = 0;
			try {
				while((length = brRubyScripts.read(buff,0,1024)) != -1){
					oswRubyScript.write(buff,0,length);
				}
				oswRubyScript.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			ZipEntry entry;
			ZipFile zRbScripts = new ZipFile(getFileStreamPath(RBSCRIPTS));
			Enumeration e = zRbScripts.entries();

			String root = getFileStreamPath(RBSCRIPTS).getParent();

			String dir="";
			while((entry = (ZipEntry) e.nextElement())!=null) {
				Log.i(TAG, entry.getName());

				if(entry.isDirectory()) { 
					dir = entry.getName();
					File file = new File(root + "/" + dir);
					file.mkdir();
				} else {
					BufferedWriter bfw = new BufferedWriter(new FileWriter(root + "/" + entry.getName()), 8);
					BufferedReader bfr = new BufferedReader(new InputStreamReader(zRbScripts.getInputStream(entry)), 8);

					String line;
					while ((line = bfr.readLine())!=null) {
						bfw.write(line+"\n");
					}
					bfw.close();
				}

				if(!e.hasMoreElements()) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
