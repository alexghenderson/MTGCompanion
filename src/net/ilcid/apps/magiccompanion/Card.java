package net.ilcid.apps.magiccompanion;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Card {
	private String mSet;
	private String mName;
	private String mScan;
	private String mType;
	private String mCost;
	private String mMechanics;
	private String mDescription;
	private Bitmap mBitmap;
	
	public Card(String name, 
			String set, 
			String scan, 
			String type, 
			String cost, 
			String mechanics, 
			String description) {
		this.mName = name;
		this.mSet = set;
		this.mScan = scan;
		this.mType = type;
		this.mCost = cost;
		this.mMechanics = mechanics;
		this.mDescription = description;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public String getSet() { 
		return this.mSet;
	}

	public String getScan() {
		return this.mScan;
	}
	
	public String getType() {
		return this.mType;
	}
	
	public String getCost() {
		return this.mCost;
	}
	
	public String getMechanics() {
		return this.mMechanics;
	}
	
	public String getDescription() {
		return this.mDescription;
	}
	
	public Bitmap getScanBitmap() throws IOException {
		if(mBitmap == null) {
			URL imageURL;
			try {
				imageURL = new URL(this.mScan);
				HttpURLConnection c = (HttpURLConnection)imageURL.openConnection();
				c.connect();
				InputStream stream = c.getInputStream();
				
				mBitmap = BitmapFactory.decodeStream(stream);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mBitmap;
	}
}
