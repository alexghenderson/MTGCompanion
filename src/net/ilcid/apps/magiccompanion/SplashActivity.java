package net.ilcid.apps.magiccompanion;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity implements Observer{
	
	private final int DIALOG_MOBILE_WARNING = 1;
	
	private int prevState = 0;
	private TextView statusView;
	private TextView progressView;
	private ProgressBar progressBarView;

	public void update(Observable observable, Object data) {
		final Download d = (Download)observable;
		final Activity activity= this;
		runOnUiThread(new Runnable() {
			
			public void run() {
				int percent = (int)d.getProgress();
				progressBarView.setProgress(percent);
				progressView.setText(String.valueOf(percent)+"%");
				if(prevState != d.getStatus())
				{
					switch(d.getStatus()){
					case Download.STATUS_DOWNLOADING:
						statusView.setText("Downloading Database");
						break;
					case Download.STATUS_ERROR:
						statusView.setText("Download Failed. Card Search Disabled");
						break;
					case Download.STATUS_COMPLETE:
			    		Intent i = new Intent(activity, MTGCompanionActivity.class);
			    		startActivity(i);
						break;
					default:
						break;
					}
				}
				prevState = d.getStatus();
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private int getNetworkType() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		if(info == null)
			return -1;
		return info.getType();
	}
	
	private void startDownload() {
		statusView.setText("Connecting");
    	progressBarView.setVisibility(View.VISIBLE);
		try {
			CardDB.Initialize(this, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void proceed() {
		Intent i = new Intent(this, MTGCompanionActivity.class);
		startActivity(i);
	}
	
	void showWarning() {
		DialogFragment fragment = new WarningDialogFragment();
		fragment.show(getFragmentManager(), "dialog");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.splash);
        getActionBar().hide();
    	statusView = (TextView) findViewById(R.id.splash_download_status);
    	progressView = (TextView) findViewById(R.id.splash_download_progress);
    	progressBarView = (ProgressBar) findViewById(R.id.splash_download_progressbar);
    	
    	Toast.makeText(this, "Checking Card Database", Toast.LENGTH_SHORT).show();
        if(CardDB.needsInit(this))
        {
        	int network = getNetworkType();
        	if(network == ConnectivityManager.TYPE_WIFI) {
        		startDownload();
        	}
        	else if(network == -1) {
        		proceed();
        	}
        	else {
        		showWarning();
        	}
        } else {
        	proceed();
        }
        
		super.onCreate(savedInstanceState);
	}
	
	public class WarningDialogFragment extends DialogFragment {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			builder.setMessage("You are on a mobile connection. Do you want to download the card database? This can be done later from the settings.");
			
			builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						startDownload();
						
					}
				});
			
			builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						proceed();
						
					}
				});
			return builder.create();
		}
	}

}
