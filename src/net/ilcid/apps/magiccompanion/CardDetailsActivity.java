package net.ilcid.apps.magiccompanion;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class CardDetailsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		overridePendingTransition(android.R.anim.slide_out_right, 0);
		
		setContentView(R.layout.card_details);
		
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}
}
