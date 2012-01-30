package net.ilcid.apps.magiccompanion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ManagePlayersActivity extends Activity {
	private Game mGame;
	private PlayerAdapter mPlayerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.players);
		mGame = Game.getInstance();
		mPlayerAdapter = new PlayerAdapter(this, R.layout.player_row, mGame.getPlayers());
		PlayerListView v = (PlayerListView)findViewById(R.id.players_list);
		v.setAdapter(mPlayerAdapter);
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.players, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;
    	switch(item.getItemId()) {
    	case android.R.id.home:
    		i = new Intent(this, MTGCompanionActivity.class);
    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(i);
    		
    	case R.id.menu_add_player:
    		return true;
    		
    	case R.id.menu_delete_player:
    		return true;
    	default: 
    		return super.onOptionsItemSelected(item);
    	}
    	
    }
	
}
