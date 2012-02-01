package net.ilcid.apps.magiccompanion;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadCardDBDialogFragment extends DialogFragment implements Observer {
	private ProgressBar progressBarView; 
	private TextView progressView;
	private TextView statusView;
	private int prevState;
	
	Download mDownload;
	
	
	public void update(Observable observable, Object data) {
		if(mDownload != null) {
			getActivity().runOnUiThread(new Runnable() {
				
				public void run() {
					int percent = (int)mDownload.getProgress();
					progressBarView.setProgress(percent);
					progressView.setText(String.valueOf(percent)+"%");
					
					int curState = mDownload.getStatus();
					if(prevState != curState)
					{
						switch(curState){
						case Download.STATUS_DOWNLOADING:
							getDialog().setTitle("Downloading Database");
							break;
						case Download.STATUS_ERROR:
							Toast.makeText(getActivity(), "Download Error", Toast.LENGTH_SHORT).show();
							dismiss();
							break;
						case Download.STATUS_COMPLETE:
							Toast.makeText(getActivity(), "Download Complete", Toast.LENGTH_SHORT).show();
							dismiss();
							break;
						default:
							break;
						}
					}
					prevState = mDownload.getStatus();
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		
		getDialog().requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		View v = inflater.inflate(R.layout.download_carddb_dialog, container, false);
		
		
		prevState = 0;
		
    	progressView = (TextView) v.findViewById(R.id.splash_download_progress);
    	progressBarView = (ProgressBar) v.findViewById(R.id.splash_download_progressbar);
    	
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		setCancelable(false);
		
		try {
			mDownload = new Download(getString(R.string.card_db_url), getActivity().getDatabasePath(getString(R.string.card_db_name)));
			mDownload.addObserver(this);
			mDownload.start();
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
			getActivity().runOnUiThread(new Runnable() {
				
				public void run() {
					Toast.makeText(getActivity(), "Download Error", Toast.LENGTH_SHORT).show();
					dismiss();
				}
			});
		}
	}
	
}
