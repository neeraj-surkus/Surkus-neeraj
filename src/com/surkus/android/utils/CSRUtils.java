package com.surkus.android.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.facebook.Session;

public class CSRUtils {
	
	private static boolean bisUserLoggedIn = false;

	public static boolean isNetworkOn(Context context) {
		if (context == null)
			return false;
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conn.getActiveNetworkInfo();

		if (networkInfo != null) {
			return networkInfo.isConnected();
		}

		return false;
	}
	
	public static void facebookLogout(Context inContext) {
		final Session mSession = Session.getActiveSession();
		if (mSession != null ) {
			mSession.closeAndClearTokenInformation();
			mSession.close();
			Session.setActiveSession(null);				
	/*		mSession = new Session(inContext);
		    Session.setActiveSession(mSession);
		    mSession.closeAndClearTokenInformation();*/
			
			//SessionStore.clearFacebookInformation(Activity.this);
		}
		
		
	/*	 if (mSession != null || mSession.getState().isClosed()) {
			 mSession.removeCallback(mSession.);
             Session session = new Session.Builder(inContext).setApplicationId(inContext.getString(R.string.app_id)).build();
             Session.setActiveSession(session);
             mSession = session;
         }*/
		
	/*	 AccountManager accountManager = (AccountManager) inContext.getSystemService(Context.ACCOUNT_SERVICE);
		 Account[] accountsList = accountManager.getAccountsByType("com.facebook.auth.login"); 
		 if(accountsList != null && accountsList.length > 0)
		 {
		   for(int i=0;i<accountsList.length;i++)
		   {
		      accountManager.removeAccount(accountsList[i], null, null);
		   }
		 }*/
	}
	
	public static boolean isUserLoggedIn()
	{
		
		return bisUserLoggedIn;
	}

	public static void setUserLoggedIn(boolean isUserLoggedIn)
	{	
		 bisUserLoggedIn = isUserLoggedIn;
	}

	
	public static void getHashKey(Context inContext) {
		try {
			PackageInfo info = inContext.getPackageManager().getPackageInfo(
					"com.surkus.android", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:","KeyHash is : "+Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	public static void showAlertDialog(final Context inContext, String inTitle,
			String inMessage) {
		try {
			Builder userAlertDialog = new Builder(inContext);
			userAlertDialog.setTitle(inTitle);
			userAlertDialog.setMessage(inMessage);
			userAlertDialog.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			userAlertDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createStringSharedPref(Context context,String key,String value){
		SharedPreferences appShPref = context.getSharedPreferences(CSRConstants.SURKUS_APP_PREFERENCE, Context.MODE_PRIVATE);
		Editor mEditor = appShPref.edit();
		mEditor.putString(key, value);
		mEditor.commit();
	}
	
	public static String getStringSharedPref(Context context,String key) {
		SharedPreferences appShPref = context.getSharedPreferences(CSRConstants.SURKUS_APP_PREFERENCE, Context.MODE_PRIVATE);
		return appShPref.getString(key, "");
	}
	
	public static void createIntSharedPref(Context context,String key,int value){
		SharedPreferences appShPref = context.getSharedPreferences(CSRConstants.SURKUS_APP_PREFERENCE, Context.MODE_PRIVATE);
		Editor mEditor = appShPref.edit();
		mEditor.putInt(key, value);
		mEditor.commit();
	}
	
	public static int getIntSharedPref(Context context,String key) {
		SharedPreferences appShPref = context.getSharedPreferences(CSRConstants.SURKUS_APP_PREFERENCE, Context.MODE_PRIVATE);
		return appShPref.getInt(key, 0);
	}
	
	
	public static void createBooleanSharedPref(Context context,String key,boolean value){
		SharedPreferences appShPref = context.getSharedPreferences(CSRConstants.SURKUS_APP_PREFERENCE, Context.MODE_PRIVATE);
		Editor mEditor = appShPref.edit();
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}
	
	public static boolean getBooleanSharedPref(Context context,String key) {
		SharedPreferences appShPref = context.getSharedPreferences(CSRConstants.SURKUS_APP_PREFERENCE, Context.MODE_PRIVATE);
		return appShPref.getBoolean(key, false);
	}
	
	public static void logoutSurkusUser(Context context) {
		SharedPreferences appShPref = context.getSharedPreferences(CSRConstants.SURKUS_APP_PREFERENCE, Context.MODE_PRIVATE);
		Editor mEditor = appShPref.edit();
		mEditor.clear();
		mEditor.commit();
	}
	
	public static String getUSFormattedMobileNumber(Double mobileNumber)
	{
		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setGroupingSeparator(CSRConstants.US_MOBILE_NUMBER_SEPRATOR);	
		DecimalFormat formatter = new DecimalFormat(CSRConstants.US_MOBILE_NUMBER_PATTERN,unusualSymbols);
		return  formatter.format(mobileNumber);

	}
	
	public static void openURLInBrowser(Context context,String URL)
	{
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(URL));
		context.startActivity(i);
	}
	
	public static void sendEmail(Activity activity,String shareMessage,String imagePath)
	{
		String mailTo="";
		Intent email_intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",mailTo, null)); 
		//Intent email_intent = new Intent(Intent.ACTION_SEND); 
		email_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Get Paid to Party with SURKUS!");  // file:///android_asset/
		email_intent.putExtra(android.content.Intent.EXTRA_TEXT, "Here's an exclusive invite for you to join SURKUS so you can get paid to party too! Sign-up at https://app.surkus.com"); 
//		email_intent.putExtra(android.content.Intent.EXTRA_TEXT,"This is awesome property near by Gurgaon must visit."); 
//		activity.startActivity(Intent.createChooser(email_intent, "Send email..."));
		
	//	email_intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));
	//	email_intent.setType("image/*");
		activity.startActivity(email_intent);
		
	}
	
	
	public static void shareOnTwitterEmail(Activity activity,String description,String shareMessage)
	{
		  Intent twitterShareIntent = new Intent(Intent.ACTION_VIEW);
		  String twitterShareURL="https://twitter.com/intent/tweet?"+"text="+description; //+"&url=" +shareMessage ;
		  twitterShareIntent.setData(Uri.parse(twitterShareURL));
		  activity.startActivity(twitterShareIntent);
	}
	
	public static void sendMessage(Activity activity,String shareMessage)
	{
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);         
		sendIntent.setData(Uri.parse("sms:"));
		sendIntent.putExtra("sms_body", shareMessage); 
		activity.startActivity(sendIntent);
	}
	
	
	 public static void clearSessionCookie(Context context) {
	    	CookieSyncManager.createInstance(context);
		    CookieManager cookieManager = CookieManager.getInstance();
		    cookieManager.removeSessionCookie();
	    }
	 
	 public static String getDeviceID(Context context) {
		    String deviceID  = "";  
		    TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		   
		    if(telephonyMgr.getDeviceId() != null && !TextUtils.isEmpty(telephonyMgr.getDeviceId()))
		       deviceID = telephonyMgr.getDeviceId();
		    else if(Secure.getString(context.getContentResolver(), Secure.ANDROID_ID) != null && !TextUtils.isEmpty(Secure.getString(context.getContentResolver(), Secure.ANDROID_ID)))
		    	deviceID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		    else
		    {
		    	  WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);   
		    	  
		    	  if(wifiManager.getConnectionInfo().getMacAddress() != null && !TextUtils.isEmpty(wifiManager.getConnectionInfo().getMacAddress()))
		    	     deviceID = wifiManager.getConnectionInfo().getMacAddress();
		    	  else  if(telephonyMgr.getSimSerialNumber() != null && !TextUtils.isEmpty(telephonyMgr.getSimSerialNumber()))
		    			  deviceID = telephonyMgr.getSimSerialNumber();
		    	  else 
		    		  deviceID = UUID.randomUUID().toString();
		    }
		    
		    return deviceID;
	    }
}
