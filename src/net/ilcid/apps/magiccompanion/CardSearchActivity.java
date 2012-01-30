package net.ilcid.apps.magiccompanion;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class CardSearchActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.card_search);
		
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
	}

}
