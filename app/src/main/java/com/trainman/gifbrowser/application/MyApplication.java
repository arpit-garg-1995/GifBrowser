package com.trainman.gifbrowser.application;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.trainman.gifbrowser.R;
import com.trainman.gifbrowser.supporting_files.GeneralInterface;

public class MyApplication extends Application {

    private static MyApplication myApplication;

    public static MyApplication getInstance() {
        return myApplication;
    }

    private RequestQueue requestQueue;
    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public String tredingUrl = "https://api.giphy.com/v1/gifs/trending?api_key=lwuzDJ2cDm2WI2bqQfHLzQuW5tLpZVT6&limit=25&rating=g";
    private ConnectivityManager manager;
    private GeneralInterface noInternetActivityListener;
    private boolean showingSWW = false;
    private boolean showingNoInternetScreen = false;

    public boolean isShowingNoInternetScreen() {
        return showingNoInternetScreen;
    }

    public void setShowingNoInternetScreen(boolean showingNoInternetScreen) {
        this.showingNoInternetScreen = showingNoInternetScreen;
    }

    public void setNoInternetActivityListener(GeneralInterface noInternetActivityListener) {
        this.noInternetActivityListener = noInternetActivityListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        setNetworkCallBackManager();
    }

    public void showNoInternetScreen(Context context){
        if (!isShowingNoInternetScreen()){
            setShowingNoInternetScreen(true);
            Intent intent = new Intent(context, NoInternetActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
    }

    public void showSomethingWentWrong(Context context){
        if (!showingSWW){
            showingSWW = true;
            new AlertDialog.Builder(context)
                    .setTitle(getString(R.string.error))
                    .setMessage(getString(R.string.try_again))
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            showingSWW = false;
                        }
                    })
                    .show();
        }
    }

    public void setNetworkCallBackManager(){
        manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if(manager != null){
            manager.registerNetworkCallback(new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build(),callback);
        }
    }

    private ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback(){
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            if (internetIsConnected() && noInternetActivityListener != null){
                noInternetActivityListener.doSomething("available",null);
            }
        }

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            Log.d("UNAVAILABLE","Internet is unavailable");
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Log.d("LOST","Internet is LOST");
        }

        @Override
        public void onLosing(@NonNull Network network, int maxMsToLive) {
            super.onLosing(network, maxMsToLive);
            Log.d("LOSING","losing until -> " + maxMsToLive);
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            Log.d("CAPABILITYCHANGED","changeInternet->" + networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET));

        }
    };

    public void unregisterNetCallback(){
        manager.unregisterNetworkCallback(callback);
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onTerminate() {
        unregisterNetCallback();
        super.onTerminate();
    }
}
