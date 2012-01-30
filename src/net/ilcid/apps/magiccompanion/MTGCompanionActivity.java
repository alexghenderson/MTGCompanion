package net.ilcid.apps.magiccompanion;

import java.io.IOException;

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
import android.view.Window;
import android.widget.Toast;

public class MTGCompanionActivity extends Activity {
	
	private Game pActiveGame;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        if(CardDB.needsInit(this))
        {
			try {
				CardDB.Initialize(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "IO Exception", 5000);
				e.printStackTrace();
			}
        } else {
        	Toast.makeText(this, "Database Found", 5000);
        }
        
		
        
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
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menu_players:
    		Intent i = new Intent(this, ManagePlayersActivity.class);
    		startActivity(i);
    		return true;
    	default: 
    		return super.onOptionsItemSelected(item);
    	}
    	
    }
    
    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
    	private final Activity mActivity;
    	private final String mTag;
    	private final Class<T> mClass;
    	private final Bundle mArgs;
    	private Fragment mFragment;
    	
    	public TabListener(Activity activity, String tag, Class<T> clz) {
    		this(activity, tag, clz, null);
    	}
    	
    	public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
    		mActivity = activity;
    		mTag = tag;
    		mClass = clz;
    		mArgs = args;
    		
    		mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
    		if(mFragment != null && !mFragment.isDetached()) {
    			FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
    			ft.detach(mFragment);
    			ft.commit();
    		}
    	}
    	
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
			
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if(mFragment == null) {
				mFragment = Fragment.instantiate(mActivity,  mClass.getName(), mArgs);
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.attach(mFragment);
			}
			
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if(mFragment != null) {
				ft.detach(mFragment);
			}
			
		}
    	
    }
}