package net.ilcid.apps.magiccompanion;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		final Activity activity = getActivity();
		
		mCardDb = new CardDB(activity);

		final EditText edit = (EditText) getView().findViewById(R.id.card_search_query);
		
		mAdapter = new CardAdapter(getActivity(), R.layout.card_row, mCardDb.getCardsByName(edit.getText().toString(), 10));
		
		CardListView listView = (CardListView) getView().findViewById(R.id.card_list);
		
		listView.setAdapter(mAdapter);
		
		edit.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mAdapter.clear();
				if(!mBusy) {
					new Thread(new Runnable() {
						public void run() {
							mBusy = true;
							final ArrayList<Card> list = mCardDb.getCardsByName(edit.getText().toString(), 10);
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
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.card_search_fragment, container, false);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
