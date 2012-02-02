package net.ilcid.apps.magiccompanion;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CardSearchFragment extends Fragment {
	
	private CardAdapter mAdapter;
	private CardDB mCardDb;
	private boolean mBusy;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mBusy = false;
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
	
    	if(CardDB.needsInit(getActivity()))
        {
    		NoCardDBAlertDialogFragment fragment = new NoCardDBAlertDialogFragment();
    		fragment.show(getFragmentManager(), null);
        }
    		
		
		View v = inflater.inflate(R.layout.card_search_fragment, container, false);
		final Activity activity = getActivity();
		
		mCardDb = new CardDB(activity);

		final EditText edit = (EditText) v.findViewById(R.id.card_search_query);
		
		//mAdapter = new CardAdapter(getActivity(), R.layout.card_row, mCardDb.getCardsByName(edit.getText().toString(), getActivity().getSharedPreferences("p.xml", 0).getInt("searchResultLimit", 10)));
		mAdapter = new CardAdapter(getActivity(), R.layout.card_row, new ArrayList<Card>());
		
		CardListView listView = (CardListView) v.findViewById(R.id.card_list);
		
		listView.setAdapter(mAdapter);
		
		edit.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(prefs.getBoolean("realTimeSearch", true)){
					mAdapter.clear();
					if(!mBusy) {
						new Thread(new Runnable() {
							public void run() {
								mBusy = true;
								final ArrayList<Card> list = mCardDb.getCardsByName(edit.getText().toString(), Integer.valueOf(prefs.getString("searchResultLimit", "30")), prefs.getString("defaultSearchType", CardDB.SEARCHTYPE_CONTAINS));
								activity.runOnUiThread(new Runnable() {
		
									public void run() {
										mAdapter.addAll(list);
										mBusy = false;
										
									}
									
								});
							}
							
						}).start();
					}
				}
				
			}
			
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				switch(actionId) {
				case EditorInfo.IME_ACTION_SEND:
					search();
					return true;
				case EditorInfo.IME_NULL:
					search();
					return true;
				case EditorInfo.IME_ACTION_SEARCH:
					search();
					return true;
				case EditorInfo.IME_ACTION_DONE:
					search();
					return true;
				}
				if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					search();
					return true;
				}
				
				return false;
			}
			
			public void search() {
				mAdapter.clear();
				new Thread(new Runnable() {
					public void run() {
						final ArrayList<Card> list = mCardDb.getCardsByName(edit.getText().toString(), Integer.valueOf(prefs.getString("searchResultLimit", "30")), prefs.getString("defaultSearchType", CardDB.SEARCHTYPE_CONTAINS));
						activity.runOnUiThread(new Runnable() {

							public void run() {
								mAdapter.addAll(list);
								
							}
							
						});
					}
					
				}).start();
			}
		});
		
		return v;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	

	public class NoCardDBAlertDialogFragment extends DialogFragment {
		
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			builder.setMessage("No card database found. Download it now?\nNote: The card database is roughly 10MB in size.");
			
			builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						DownloadCardDBDialogFragment fragment = new DownloadCardDBDialogFragment();
			    		fragment.show(getFragmentManager(), null);
					}
				});
			builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					getActivity().finish();
					
				}
			});
			builder.setCancelable(false);
			return builder.create();
		}
	}
}
