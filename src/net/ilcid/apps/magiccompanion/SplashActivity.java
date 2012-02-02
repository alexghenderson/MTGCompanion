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
import android.preference.PreferenceManager;
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

	void showWarning() {
		DialogFragment fragment = new NoCardDBDialogFragment();
		fragment.show(getFragmentManager(), "dialog");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.splash);
        getActionBar().hide();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	if(prefs.getBoolean("askCardDBDownload", true) && CardDB.needsInit(this))
        {
        	showWarning();
        } else {
    		Intent i = new Intent(this, MTGCompanionActivity.class);
    		startActivity(i);
        }
        
		super.onCreate(savedInstanceState);
	}
	
	public class NoCardDBDialogFragment extends DialogFragment {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			builder.setMessage("No card database found. Would you like to download it now?");
			
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
