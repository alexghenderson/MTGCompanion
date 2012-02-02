package net.ilcid.apps.magiccompanion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class CardDB {
	public static final String SEARCHTYPE_CONTAINS = "1";
	public static final String SEARCHTYPE_BEGINS_WITH = "2";
	public static final String SEARCHTYPE_ENDS_WITH = "3";
	public static final String SEARCHTYPE_EXACT_MATCH = "4";
	
	public static final String KEY_SET = "set_code";
	public static final String KEY_NAME = "name";
	public static final String KEY_SCAN = "scan";
	public static final String KEY_TYPE = "card_type";
	public static final String KEY_COST = "cost";
	public static final String KEY_MECHANICS = "mechanics";
	public static final String KEY_DESCRIPTION = "description";
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_URL = "http://ldap.ilcid.net/cards.db";
	private static final String DATABASE_PATH = "/data/data/";
	private static final String DATABASE_NAME = "cards.db";
	private static final String DATABASE_TABLE = "cards";
	private static final int DATABASE_VERSION = 2;
	
	private final Context mCtx;
	private static boolean mReady;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
			
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
		
	}

	private static boolean checkDb(Context c) {
		SQLiteDatabase db = null;
		try {
			db = SQLiteDatabase.openDatabase(c.getDatabasePath(DATABASE_NAME).getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		if(db != null) {
			db.close();
		}
		return (db == null) ? true : false;
	}
	
	private static void copyDb(final Context c, Observer o) throws IOException {
		URL url;
		url = new URL(DATABASE_URL);
		Download dl = new Download(url, c.getApplicationContext().getDatabasePath(DATABASE_NAME));
		dl.addObserver(o);
		dl.start();
	}
	
	public static boolean needsInit(Context c) {
		return checkDb(c);
	}
	
	public static void Initialize(Context c, Observer o) throws IOException {
		copyDb(c, o);
		mReady = checkDb(c);
	}
	
	public static boolean isReady() {
		return mReady;
	}
	
	public CardDB(Context ctx) {
		mCtx = ctx;
	}
	
	public CardDB open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getReadableDatabase();
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	public ArrayList<Card> getCardsByName(String qname, int limit, String queryType) {
		if(mDb == null)
			open();
		
		switch(Integer.valueOf(queryType)) {
		case 1:
			qname = "%"+qname+"%";
			break;
		case 2:
			qname = qname+"%";
			break;
		case 3:
			qname = "%"+qname;
			break;
		default:
			break;
		}
		Cursor c = null;
			c = mDb.query(DATABASE_TABLE, new String [] {
					KEY_SET,
					KEY_NAME,
					KEY_SCAN,
					KEY_COST,
					KEY_TYPE,
					KEY_MECHANICS,
					KEY_DESCRIPTION
			}, KEY_NAME + " LIKE ?" ,  new String [] {qname}, KEY_NAME, null, KEY_NAME + " LIMIT " + String.valueOf(limit));
		ArrayList<Card> list = new ArrayList<Card>();
		if(c != null) {
			c.moveToFirst();
			while(!c.isAfterLast()) {
				String name = c.getString(c.getColumnIndex(KEY_NAME));
				String set = c.getString(c.getColumnIndex(KEY_SET));
				String scan = c.getString(c.getColumnIndex(KEY_SCAN));
				String cost = c.getString(c.getColumnIndex(KEY_COST));
				String type = c.getString(c.getColumnIndex(KEY_TYPE));
				String mechanics = c.getString(c.getColumnIndex(KEY_MECHANICS));
				String description = c.getString(c.getColumnIndex(KEY_DESCRIPTION));
				list.add(new Card(name, set, scan, type, cost, mechanics, description));
				c.moveToNext();
			}
		}
		return list;
	}
	
	public Card getCardByName(String qname) {
		if(mDb == null)
			open();
		
		Cursor c = null;
			c = mDb.query(DATABASE_TABLE, new String [] {
					KEY_SET,
					KEY_NAME,
					KEY_SCAN,
					KEY_COST,
					KEY_TYPE,
					KEY_MECHANICS,
					KEY_DESCRIPTION
			}, KEY_NAME + " = ?", new String [] {qname}, null, null, KEY_NAME + " LIMIT 1");
		Card card = null;
		if(c != null) {
			c.moveToFirst();
			String name = c.getString(c.getColumnIndex(KEY_NAME));
			String set = c.getString(c.getColumnIndex(KEY_SET));
			String scan = c.getString(c.getColumnIndex(KEY_SCAN));
			String cost = c.getString(c.getColumnIndex(KEY_COST));
			String type = c.getString(c.getColumnIndex(KEY_TYPE));
			String mechanics = c.getString(c.getColumnIndex(KEY_MECHANICS));
			String description = c.getString(c.getColumnIndex(KEY_DESCRIPTION));
			card = new Card(name, set, scan, type, cost, mechanics, description);
		}
		return card;
	}
	
}
