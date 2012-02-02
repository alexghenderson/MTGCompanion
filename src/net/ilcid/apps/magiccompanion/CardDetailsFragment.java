package net.ilcid.apps.magiccompanion;

import java.io.IOException;

import net.ilcid.apps.magiccompanion.CardSearchFragment.NoCardDBAlertDialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class CardDetailsFragment extends Fragment {
	private CardDB mCardDb;
	private Card mCard;
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		Bundle extras = getActivity().getIntent().getExtras();
		if(extras != null && extras.containsKey("card_name")) {
			String cardName = extras.getString("card_name");
			if(!CardDB.needsInit(getActivity()))
			{
				mCardDb = new CardDB(getActivity());
				if(mCardDb != null) {
					mCard = mCardDb.getCardByName(cardName);
				}
			}
		}
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.card_details_fragment, container, false);
		getActivity().setProgressBarIndeterminateVisibility(false);

		if(mCard != null) {
			getActivity().setTitle(mCard.getName());
			/*TextView nameView = (TextView)v.findViewById(R.id.card_details_name);
			TextView typeView = (TextView)v.findViewById(R.id.card_details_type);
			TextView costView = (TextView)v.findViewById(R.id.card_details_cost);
			TextView mechanicsView = (TextView)v.findViewById(R.id.card_details_mechanics);
			TextView descriptionView = (TextView)v.findViewById(R.id.card_details_description);
			final ImageView scanView = (ImageView)v.findViewById(R.id.card_details_scan);
			nameView.setText(mCard.getName());
			typeView.setText(mCard.getType());
			costView.setText(mCard.getCost());
			mechanicsView.setText(mCard.getMechanics());
			descriptionView.setText(mCard.getDescription());*/
		}
		
		return v;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		super.onStart();
		if(mCard == null) {
			InvalidCardDialogFragment fragment = new InvalidCardDialogFragment();
			fragment.show(getFragmentManager(), null);
		}
		else {
			if(prefs.getBoolean("autoDownloadScans", true) || mCard.scanReady()) {
				getActivity().setProgressBarIndeterminateVisibility(true);
				getActivity().getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_START);
				new Thread(new Runnable() {
					
					public void run() {
						try {
							mCard.getScanBitmap();
							getActivity().runOnUiThread(new Runnable() {
								
								public void run() {
									try {
										ImageView iv = (ImageView)(getView().findViewById(R.id.card_details_scan));
										iv.setImageBitmap(mCard.getScanBitmap());
										getActivity().setProgressBarIndeterminateVisibility(false);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
							});
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}).start();
			}
		}
	}
	
	public class InvalidCardDialogFragment extends DialogFragment {

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			builder.setMessage("Unable to retrieve card information.");
			
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						getActivity().finish();
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
}
