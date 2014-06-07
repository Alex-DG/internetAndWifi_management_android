package com.project.connectionmanagement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/*
 * Class which check internet connection status
 */
public class InternetConnectionDetector {
	
	private Context _context;
	private WifiManager _wifiManager;
	
	/*
	 * Constructor
	 */
	public InternetConnectionDetector(Context context, WifiManager wifiManager){
		this._context = context;
		this._wifiManager = wifiManager;
	}
	
	/*
	 * Return if there is internet connection status or not
	 */
	public boolean isConnecting(){
		ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(connectivityManager != null){
			NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
			
			if(info != null){
				for(int index = 0; index < info.length; index++){
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * Update wifi status
	 */
	public boolean isWifiConnected(){
		return _wifiManager.isWifiEnabled();		
	}
}
