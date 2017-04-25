package com.skull.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.skull.R;


public class ConnectionDetector {
	private Context _context;

	public ConnectionDetector(Context context) {
		this._context = context;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public void alertForInternet() {
		AlertDialog.Builder internetAlert = new AlertDialog.Builder(_context);
		internetAlert.setTitle("ERROR");
		internetAlert
				.setMessage("No internet Connection, Please connect to a working internet connection");
		internetAlert.setIcon(R.drawable.fail);
		internetAlert.setCancelable(false);

		internetAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		AlertDialog alert = internetAlert.create();
		alert.show();
	}

}