package com.project.connectionmanagement;

import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	boolean isInternetDetected = false;
	boolean wifiStatus = false; 
	boolean networkStatus = false;
	
	InternetConnectionDetector internetConnectionDetector;	
	WifiManager wifiManager;	
	
	ConnectivityManager dataManager;
	Method dataMtd;
	NetworkInfo networkInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnInternetStatus = (Button) findViewById(R.id.btn_check);
		
		Button btnEnableWifi = (Button) findViewById(R.id.btn_enable_wifi);
		Button btnDisableWifi = (Button) findViewById(R.id.btn_disable_wifi);
		
		Button btnEnableInternet = (Button) findViewById(R.id.btn_enable_data_connection);
		Button btnDisableInternet = (Button) findViewById(R.id.btn_disable_data_connection);
		
		try
		{
			wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);	
			internetConnectionDetector = new InternetConnectionDetector(getApplicationContext(), wifiManager);			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		btnInternetStatus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				isInternetDetected = internetConnectionDetector.isConnecting();
				
				//Check if internet is enabled
				if(isInternetDetected)
				{
					setAlertDialogStatus(MainActivity.this, "Internet Connection", "You have internet connection", isInternetDetected);
				} 
				else 
				{
					setAlertDialogStatus(MainActivity.this, "No Internet Connection","You don't have internet connection.", isInternetDetected);
				}
			}
		});
		
		btnEnableWifi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				//check wifi status
				wifiStatus = internetConnectionDetector.isWifiConnected();
				
				//if wifi is disabled => enable wifi
				if(!wifiStatus) 
				{
					setAlertDialogWifiActiviation(MainActivity.this, "Wifi activation", "Do you want to activate your Wifi ?", wifiStatus);					
				}
				else
				{
					setAlertDialogInformation(MainActivity.this, "Wifi activation", "Wifi is already activated on your device.");
				}
			}
		});
		
		btnDisableWifi.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				
				wifiStatus = internetConnectionDetector.isWifiConnected();
				
				//if wifi is enable => disable wifi
				if(wifiStatus)
				{
					setAlertDialogWifiActiviation(MainActivity.this, "Wifi activation", "Do you want to disable Wifi ?", wifiStatus);
				} 
				else
				{
					setAlertDialogInformation(MainActivity.this, "Wifi activation", "Wifi is already disabled on your device.");
				}
			}
		});
		
		btnDisableInternet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				
				dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				networkInfo = dataManager.getActiveNetworkInfo();
				
				if(networkInfo != null)
				{
					networkStatus = true;
					setAlertDialogInternetActiviation(MainActivity.this, "Internet Activation", "Do you want to disable internet ?", networkStatus);
				}
				else
				{
					setAlertDialogInformation(MainActivity.this, "Internet activation", "Internet is already disabled on your device.");
				}
			}
		});
		
		btnEnableInternet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				
				dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				networkInfo = dataManager.getActiveNetworkInfo();
				
				if(networkInfo == null)
				{
					setAlertDialogInternetActiviation(MainActivity.this, "Internet Activation", "Do you want to enable internet ?", networkStatus);
				}
				else
				{
					setAlertDialogInformation(MainActivity.this, "Internet activation", "Internet is already enable on your device.");
				}
			}
		});
	}
	
	/*
	 * Internet status dialog
	 */
	public void setAlertDialogStatus(Context context, String title, String message, Boolean status){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage(message)
			   .setTitle(title)
		       .setCancelable(false)
		       .setIcon((status) ? R.drawable.success : R.drawable.fail)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface arg0, int arg1) {}
		       });
		       
		AlertDialog alert = builder.create();		
		alert.show();		
	}
	
	/*
	 * Information dialog
	 */
	public void setAlertDialogInformation(Context context, String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage(message)
			   .setTitle(title)
		       .setCancelable(false)
		       .setIcon(R.drawable.information)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface arg0, int arg1) {}
		       });
		       
		AlertDialog alert = builder.create();		
		alert.show();		
	}
	
	/*
	 * Wifi activation dialog
	 */
	public void setAlertDialogWifiActiviation(Context context, String title, String message, Boolean wifiStatus){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert = null;
		
		final Boolean status = wifiStatus;
		
		builder.setMessage(message)
			   .setTitle(title)
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					   
		    	    @Override
					public void onClick(DialogInterface arg0, int arg1) {
		    	    	wifiActivation(status);						
					}
		    	    
		       })
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {				
					
		    	    @Override
					public void onClick(DialogInterface dialog, int which) {}
		    	    
				});		       
		       
		alert = builder.create();		
		alert.show();	
	}
	
	/*
	 * Internet activation dialog
	 */
	public void setAlertDialogInternetActiviation(Context context, String title, String message, Boolean internetStatus){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert = null;
		
		final Boolean status = internetStatus;
		
		builder.setMessage(message)
			   .setTitle(title)
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					   
		    	    @Override
					public void onClick(DialogInterface arg0, int arg1) {
		    	    	internetActivation(status);						
					}
		    	    
		       })
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {				
					
		    	    @Override
					public void onClick(DialogInterface dialog, int which) {}
		    	    
				});		       
		       
		alert = builder.create();		
		alert.show();	
	}
	
	/*
	 * Manage wifi activation
	 */
	private void wifiActivation(boolean activation) {
		
		if(!activation)
		{
			try
			{		
				wifiManager.setWifiEnabled(true);
				setAlertDialogStatus(MainActivity.this, "Wifi activation","Wifi has been activated with success.", true);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				setAlertDialogStatus(MainActivity.this, "Wifi activation","Error on wifi activation.", false);
			}				
		}
		else
		{
			try
			{		
				wifiManager.setWifiEnabled(false);
				setAlertDialogStatus(MainActivity.this, "Wifi activation","Wifi has been disabled with success.", true);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				setAlertDialogStatus(MainActivity.this, "Wifi activation","Error on wifi activation.", false);
			}				
		}				
	}
	
	/*
	 * Manage internet activation
	 */
	public void internetActivation(boolean activation){
		
		try 
		{
			dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
			dataMtd.setAccessible(true);

			try
			{
				if(!activation)
				{
					try
					{		
						dataMtd.invoke(dataManager, true);
						setAlertDialogStatus(MainActivity.this, "Internet activation","Internet has been activated with success.", true);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						setAlertDialogStatus(MainActivity.this, "Internet activation","Error on Internet activation.", false);
					}				
				}
				else
				{
					try
					{		
						dataMtd.invoke(dataManager, false);
						setAlertDialogStatus(MainActivity.this, "Internet activation","Internet has been disabled with success.", true);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						setAlertDialogStatus(MainActivity.this, "Internet activation","Error on Internet activation.", false);
					}				
				}	
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
