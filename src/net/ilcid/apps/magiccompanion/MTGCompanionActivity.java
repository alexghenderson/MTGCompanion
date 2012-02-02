package net.ilcid.apps.magiccompanion;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

public class MTGCompanionActivity extends Activity {
	
	private Game pActiveGame;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        
        
        
        this.pActiveGame = Game.getInstance();
        this.pActiveGame.addPlayer("John Griffiths");
        this.pActiveGame.addPlayer("Bombadil Goh");
        this.pActiveGame.addPlayer("Phillip Slater");
        this.pActiveGame.addPlayer("John Strain");
        this.pActiveGame.addPlayer("Alex Henderson");
        this.pActiveGame.addPlayer("Joe");
        this.pActiveGame.addPlayer("Bob");
        this.pActiveGame.addPlayer("Joe");
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	return super.onTouchEvent(event);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;
    	switch(item.getItemId()) {
    	case R.id.menu_players:
    		i = new Intent(this, ManagePlayersActivity.class);
    		startActivity(i);
    		return true;
    	case R.id.menu_search:
    		i = new Intent(this, CardSearchActivity.class);
    		startActivity(i);
    		return true;
    	case R.id.menu_settings:
    		i = new Intent(this, SettingsActivity.class);
    		startActivity(i);
    		return true;
    	default: 
    		return super.onOptionsItemSelected(item);
    	}
    	
    }
}