package com.surkus.android.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

public class CSRUtils {

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

	public static void getHashKey(Context inContext) {
		try {
			PackageInfo info = inContext.getPackageManager().getPackageInfo(
					"com.surkus.android", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
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
}
