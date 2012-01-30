package net.ilcid.apps.magiccompanion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

import org.apache.http.HttpException;

public class Download extends Observable implements Runnable {

	private static final int MAX_BUFFER_SIZE = 1024;
	
	public static final int STATUS_PAUSED = 0;
	public static final int STATUS_DOWNLOADING = 1;
	public static final int STATUS_COMPLETE = 2;
	public static final int STATUS_ERROR = -1;
	
	private Thread mThread;
	
	private File mFile;
	private URL mUrl;
	private int mLength;
	private int mDownloaded;
	private int mStatus;
	
	public Download(String url) throws MalformedURLException {
		this(new URL(url));
	}
	
	public Download(URL url) {
		this(url, new File(getFilename(url)));
	}
	
	public Download(String url, File f) throws MalformedURLException {
		this(new URL(url), f);
	}
	
	public Download(URL url, File f) {
		mUrl = url;
		mLength = -1;
		mDownloaded = 0;
		mStatus = STATUS_PAUSED;
		mThread = new Thread(this);
		mFile = f;
		f.getParentFile().mkdirs();
	}
	
	public int getLength() {
		return mLength;
	}
	
	public int getDownloaded() {
		return mDownloaded;
	}
	
	public float getProgress() {
		return ((float) mDownloaded / mLength) * 100;
	}
	
	public int getStatus() {
		return mStatus;
	}
	
	public void start() {
		mStatus = STATUS_DOWNLOADING;
		stateChanged();
		mThread.start();
	}
	
	public void stop() {
		mStatus = STATUS_PAUSED;
		stateChanged();
	}
	
	public void reset() {
	}
	
	public void stateChanged() {
		setChanged();
		try{
			notifyObservers();
		} catch(RuntimeException e) {
			
		}
	}
	
	private static String getFilename(URL url) {
		String name = url.getFile();
		return name.substring(name.lastIndexOf('/')+1);
	}
	
	public void run() {
		InputStream is = null;
		RandomAccessFile file = null;
		
		try {
			HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
			conn.setRequestProperty("Range", "bytes="+mDownloaded+"-");
			conn.connect();
			
			if(conn.getResponseCode() / 100 != 2) {
				mStatus = STATUS_ERROR;
				stateChanged();
				conn.disconnect();
				throw new HttpException();
			}
			int contentLength = conn.getContentLength();
			if(contentLength < 1 ) {
				mStatus = STATUS_ERROR;
				stateChanged();
				conn.disconnect();
				throw new HttpException();
			}
			if(mLength == -1) {
				mLength = contentLength;
				stateChanged();
			}
			file = new RandomAccessFile(mFile, "rw");
			file.seek(mDownloaded);
			is = conn.getInputStream();
			while(mStatus == STATUS_DOWNLOADING) {
				byte buffer[];
				if(mLength - mDownloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				} else {
					buffer = new byte[mLength - mDownloaded];
				}
				int read = is.read(buffer);
				if(read == -1){
					break;
				}
				file.write(buffer, 0, read);
				mDownloaded += read;
				stateChanged();
			}
			conn.disconnect();
			if(mStatus == STATUS_DOWNLOADING) {
				mStatus = STATUS_COMPLETE;
				stateChanged();
			}
			
		} catch (HttpException e) {
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(mFile != null) {
				try {
					file.close();
				} catch (Exception e) {
					
				}
			}
		}
		
	}
	
	

}
