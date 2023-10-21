package com.exemple.sychronizedclock;

import android.net.ConnectivityManager;
import android.net.Network;

/*
* This is a class to keep track on if the application have internet connection or not.
* It utilizes ConnectivityManager.NetworkCallback methods.
* The methods will also set the global static boolean "connectedTo" to true/false.
 */
public class InternetChecker extends ConnectivityManager.NetworkCallback {
    @Override
    // Internet connection is available
    public void onAvailable(Network network) {
        super.onAvailable(network);
        // boolean set to false
        InternetBoolean.connectedTo = true;

    }

    @Override
    // Internet connection is lost
    public void onLost(Network network) {
        super.onLost(network);
      // boolean set to false
        InternetBoolean.connectedTo = false;
    }


}