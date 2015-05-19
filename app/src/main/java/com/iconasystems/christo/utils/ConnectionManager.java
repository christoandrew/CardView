package com.iconasystems.christo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Christo on 1/29/2015.
 */
public class ConnectionManager {
    private Context _context;
    private NetworkInfo networkInfo;
    private String mErrorMessage;


    private static final String ERROR_TITLE = "error_title";
    private String mErrorTitle;

    public ConnectionManager(Context context) {
        this._context = context;
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        } else {
            assert networkInfo != null;
            setErrorMessage("No Internet Connection");
            setErrorTitle("Error");
            return false;
        }
    }

    public String getErrorMessage(){
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage){
        this.mErrorMessage = errorMessage;
    }

    public String getErrorTitle(){
        return mErrorTitle;
    }

    public void setErrorTitle(String errorTitle){
        this.mErrorTitle = errorTitle;
    }
}
