package com.surkus.android.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
import android.util.Base64;
import android.util.Log;

import com.facebook.Session;

public class CSRUtils {
	
	public interface ShareOnFacebookInterface
	{
		public void shareOnFacebook();
	}
	
	

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
	
	public static void facebookLogout() {
		Session mSession = Session.getActiveSession();
		if (mSession != null) {
			mSession.closeAndClearTokenInformation();
			Session.setActiveSession(null);
		}
	}

	public static void getHashKey(Context inContext) {
		try {
			PackageInfo info = inContext.getPackageManager().getPackageInfo(
					"com.surkus.android", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",Base64.encodeToString(md.digest(), Base64.DEFAULT));
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
		email_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SURKUS – Get Paid to Party");  // file:///android_asset/
		email_intent.putExtra(android.content.Intent.EXTRA_TEXT, "SURKUS is a mobile platform that connects you to local hot spots and events where you are paid to simply attend and become part of their crowd.  We call it CrowdCasting."); 
//		email_intent.putExtra(android.content.Intent.EXTRA_TEXT,"This is awesome property near by Gurgaon must visit."); 
//		activity.startActivity(Intent.createChooser(email_intent, "Send email..."));
		
	//	email_intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));
	//	email_intent.setType("image/*");
		activity.startActivity(email_intent);
		
	}
	
	
	public static void shareOnTwitterEmail(Activity activity,String description,String shareMessage)
	{
		  Intent twitterShareIntent = new Intent(Intent.ACTION_VIEW);
		  String twitterShareURL="https://twitter.com/intent/tweet?"+"text="+description+"&url=" +shareMessage ;//https://twitter.com/intent/tweet?url=
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
	
	
	
}
