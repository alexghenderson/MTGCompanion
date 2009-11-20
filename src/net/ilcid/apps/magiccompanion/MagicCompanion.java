package net.ilcid.apps.magiccompanion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MagicCompanion extends Activity {
	private PowerManager.WakeLock wakeLock;
	
	private final static int MENU_SET_LIFE = 1;
	private final static int MENU_RESET_LIFE = 2;
	private final static int MENU_CARD_SEARCH = 3;
	private final static int MENU_EXIT = 4;
	
	private final static int DIALOG_SEARCH = 1;
	private final static int DIALOG_ADD_LIFE = 2;
	private final static int DIALOG_SUBTRACT_LIFE = 3;
	private final static int DIALOG_SET_LIFE = 4;
	
	private final static int LIFE_DEFAULT = 20;
	private final static int LIFE_LOW = 10;
	private final static int LIFE_HIGH = 15;
	
	private final static String KEY_LIFE = "key_life";
	
	private int life;
	
	/** Create activity menu **/
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Resources res = getResources();
		menu.add(Menu.NONE, MENU_SET_LIFE, 0, res.getString(R.string.menu_set_life));
		menu.add(Menu.NONE, MENU_RESET_LIFE, 0, res.getString(R.string.menu_reset_life));
		menu.add(Menu.NONE, MENU_CARD_SEARCH, 0, res.getString(R.string.menu_card_search));
		//menu.add(Menu.NONE, MENU_EXIT, 0, res.getString(R.string.menu_exit));
		return true;
	}
	
	/** Define menu actions **/
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case MENU_SET_LIFE:
			showDialog(DIALOG_SET_LIFE);
			return true;
		case MENU_RESET_LIFE:
			setLife(LIFE_DEFAULT);
			return true;
		case MENU_CARD_SEARCH:
			showDialog(DIALOG_SEARCH);
			return true;
		case MENU_EXIT:
			finish();
			return true;
		}
		return false;
	}
	
	/** Called first time dialog is created **/
	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog = null;
		Resources res = getResources();
		switch(id)
		{
		case DIALOG_SEARCH:
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.search_dialog);
			dialog.setTitle(res.getString(R.string.dialog_card_search_title));
			final Button searchButton = (Button) dialog.findViewById(R.id.search_button);
			final EditText searchQuery = (EditText) dialog.findViewById(R.id.dialog_card_search_query);
			searchButton.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://magiccards.info/card.php?card=" + searchQuery.getText()));
					try
					{
						startActivity(i);
					}
					catch(Exception e)
					{
					}
					dismissDialog(DIALOG_SEARCH);
				}
			});
			searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() 
			{	
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
				{
					if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
					{
						searchButton.performClick();
						return true;
					}
					return false;
				}
			});
			break;
		case DIALOG_ADD_LIFE:
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.add_life_dialog);
			dialog.setTitle(res.getString(R.string.dialog_add_life));
			final Button addButton = (Button) dialog.findViewById(R.id.dialog_add_life_button);
			final EditText addField = (EditText) dialog.findViewById(R.id.dialog_add_life_amount);
			addButton.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					try
					{
						int amount = Integer.parseInt(addField.getText().toString());
						setLife(life + amount);
					}
					catch(Exception e)
					{
						
					}
					finally
					{
						dismissDialog(DIALOG_ADD_LIFE);
					}
				}
			});
			addField.setOnEditorActionListener(new TextView.OnEditorActionListener()
			{
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
				{
					if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
					{
						addButton.performClick();
						return true;
					}
					return false;
				}
			});
			break;
		case DIALOG_SUBTRACT_LIFE:
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.subtract_life_dialog);
			dialog.setTitle(res.getString(R.string.dialog_subtract_life));
			final Button subtractButton = (Button) dialog.findViewById(R.id.dialog_subtract_life_button);
			final EditText subtractField = (EditText) dialog.findViewById(R.id.dialog_subtract_life_amount);
			subtractButton.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v) 
				{
					try
					{
						int amount = Integer.parseInt(subtractField.getText().toString());
						setLife(life - amount);
					}
					catch(Exception e)
					{
						
					}
					finally
					{
						dismissDialog(DIALOG_SUBTRACT_LIFE);
					}
					
				}
			});
			subtractField.setOnEditorActionListener(new TextView.OnEditorActionListener()
			{		
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
				{
					if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
					{
						subtractButton.performClick();
						return true;
					}
					return false;
				}
			});
			break;
		case DIALOG_SET_LIFE:
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.set_life_dialog);
			dialog.setTitle(res.getString(R.string.dialog_set_life));
			final Button setButton = (Button) dialog.findViewById(R.id.dialog_set_life_button);
			final EditText setField = (EditText) dialog.findViewById(R.id.dialog_set_life_amount);
			setButton.setOnClickListener(new View.OnClickListener()
			{	
				public void onClick(View v) 
				{
					try
					{
						int amount = Integer.parseInt(setField.getText().toString());
						setLife(amount);
					}
					catch(Exception e)
					{
		
					}
					finally
					{
						dismissDialog(DIALOG_SET_LIFE);
					}
				}
			});
			setField.setOnEditorActionListener(new TextView.OnEditorActionListener() 
			{	
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
				{
					if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
					{
						setButton.performClick();
						return true;
					}
					return false;
				}
			});
		}
		return dialog;
	}
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magiccompanion_main);
        final Button incrementButton = (Button) findViewById(R.id.increase);
        final Button decrementButton = (Button) findViewById(R.id.decrease);
        incrementButton.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				setLife(life + 1);
			}
		});
        incrementButton.setLongClickable(true);
        incrementButton.setOnLongClickListener(new View.OnLongClickListener() 
        {
			public boolean onLongClick(View v) 
			{
				showDialog(DIALOG_ADD_LIFE);
				return true;
			}
		});
        decrementButton.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				setLife(life - 1);		
			}
		});
        decrementButton.setLongClickable(true);
        decrementButton.setOnLongClickListener(new View.OnLongClickListener()
        {	
			public boolean onLongClick(View v) {
				showDialog(DIALOG_SUBTRACT_LIFE);
				return true;
			}
		});

		setLife(LIFE_DEFAULT);
    }

	public void onResume()
	{
		super.onResume();
		//Acquire wake-lock
		if(this.wakeLock == null)
		{
			PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
			this.wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MTGCompanion");
		}
		this.wakeLock.acquire();
	}
	
	public void onPause()
	{
		super.onPause();
		//Release wake-lock
		if(this.wakeLock != null)
		{
			this.wakeLock.release();
		}
	}
    
    public void setLife(int life)
    {
    	this.life = life;
    	final TextView lifeView = (TextView)findViewById(R.id.life);
    	lifeView.setText(Integer.toString(life));
    	if(life > LIFE_HIGH)
    	{
    		lifeView.setTextColor(Color.GREEN);
    	}
    	else if(life <= LIFE_HIGH && life > LIFE_LOW)
    	{
    		lifeView.setTextColor(Color.YELLOW);
    	}
    	else if(life <= LIFE_LOW)
    	{
    		lifeView.setTextColor(Color.RED);
    	}
    }

	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		if(outState != null)
		{
			outState.putInt(KEY_LIFE, Integer.valueOf(life));
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
        {
        	if(savedInstanceState.containsKey(KEY_LIFE))
        	{
        		setLife(savedInstanceState.getInt(KEY_LIFE));
        	}
        }
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) 
	{
		super.onPrepareDialog(id, dialog);
		switch(id)
		{
		case DIALOG_SEARCH:
			final EditText searchQuery = (EditText)dialog.findViewById(R.id.dialog_card_search_query);
			searchQuery.setText("");
			searchQuery.requestFocus();
			break;
		case DIALOG_ADD_LIFE:
			final EditText addLifeField = (EditText)dialog.findViewById(R.id.dialog_add_life_amount);
			addLifeField.setText("");
			addLifeField.requestFocus();
			break;
		case DIALOG_SET_LIFE:
			final EditText setLifeField = (EditText)dialog.findViewById(R.id.dialog_set_life_amount);
			setLifeField.setText("");
			setLifeField.requestFocus();
			break;
		case DIALOG_SUBTRACT_LIFE:
			final EditText subtractLifeField = (EditText)dialog.findViewById(R.id.dialog_subtract_life_amount);
			subtractLifeField.setText("");
			subtractLifeField.requestFocus();
		}
	}
}