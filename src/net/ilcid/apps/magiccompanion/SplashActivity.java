package net.ilcid.apps.magiccompanion;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	
	private final int DIALOG_MOBILE_WARNING = 1;

	
	private int getNetworkType() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		if(info == null)
			return -1;
		return info.getType();
	}

	void showNoDataAlert() {
		DialogFragment fragment = new NoDataAlertDialogFragment();
		fragment.show(getFragmentManager(), "dialog");
	}
	void showWarning() {
		DialogFragment fragment = new WarningDialogFragment();
		fragment.show(getFragmentManager(), "dialog");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.splash);
        getActionBar().hide();
        
        //Set up prefs file, default values if not already set
        SharedPreferences prefs = getSharedPreferences("p.xml", 0);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        //if(!prefs.contains("searchResultLimit"))
        	prefsEdit.putInt("searchResultLimit", 30);

        if(!prefs.contains("askCardDBDownload"))
        	prefsEdit.putBoolean("askCardDBDownload", true);

        //if(!prefs.contains("autoDownloadScans"))
        	prefsEdit.putBoolean("autoDownloadScans", true);

        if(!prefs.contains("realTimeSearch"))
        	prefsEdit.putBoolean("realTimeSearch", false);
        
        prefsEdit.commit();
    	
    	Toast.makeText(this, "Checking Card Database", Toast.LENGTH_SHORT).show();
        boolean needDownload = CardDB.needsInit(this);
        needDownload = true;
    	if(needDownload)
        {
        	int network = getNetworkType();
        	if(network == -1) {
        		showNoDataAlert();
        	}
        	else {
        		if(prefs.getBoolean("askCardDBDownload", true)) {
        			showWarning();
        		}
        		else {
					SplashDownloadCardDBDialogFragment d = new SplashDownloadCardDBDialogFragment();
					d.show(getFragmentManager(), null);
        		}
        	}
        } else {
    		Intent i = new Intent(this, MTGCompanionActivity.class);
    		startActivity(i);
        }
        
		super.onCreate(savedInstanceState);
	}
	
	public class NoDataAlertDialogFragment extends DialogFragment {

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			builder.setMessage("No network connection found. A network connection is required to download the card database. You can download the card database later from the settings.");
			
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(getActivity(), MTGCompanionActivity.class);
						startActivity(i);
					}
				});
			return builder.create();
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setCancelable(false);
		}
	}
	
	public class WarningDialogFragment extends DialogFragment {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			builder.setMessage("No card database found. Would you like to download it now? This can be done later from the settings.");
			
			builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						SplashDownloadCardDBDialogFragment d = new SplashDownloadCardDBDialogFragment();
						d.show(getFragmentManager(), null);
					}
				});
			
			builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(getActivity(), MTGCompanionActivity.class);
						startActivity(i);
						
					}
				});
			return builder.create();
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setCancelable(false);
		}
	}
	
	public class SplashDownloadCardDBDialogFragment extends DownloadCardDBDialogFragment{
		@Override
		public void onDismiss(DialogInterface dialog) {
			super.onDismiss(dialog);
			Intent i = new Intent(getActivity(), MTGCompanionActivity.class);
			startActivity(i);
		}
	}

}
