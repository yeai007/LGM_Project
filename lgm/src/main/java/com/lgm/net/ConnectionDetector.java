package com.lgm.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author Administrator
 */
public class ConnectionDetector {

    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        boolean bRet = false;
        try {
            bRet = isNetworkAvailable(_context);
        } catch (Exception e) {

            Log.e("ConnectionDetector", e.getMessage());
            bRet = false;
        }
        return bRet;
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connect == null) {
            return false;
        } else//get all network info
        {
            NetworkInfo info = connect.getActiveNetworkInfo();
            if (info != null) {
/*				for (NetworkInfo anInfo : info) {*/
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            /*	}*/
            }
        }
        return false;
    }
}