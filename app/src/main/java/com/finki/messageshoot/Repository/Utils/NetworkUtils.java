package com.finki.messageshoot.Repository.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.finki.messageshoot.R;
import com.finki.messageshoot.databinding.ActivityMainBinding;

public class NetworkUtils {

    private static NetworkUtils instance;

    private NetworkUtils() {

    }

    public static NetworkUtils getInstance() {
        if (instance == null)
            instance = new NetworkUtils();
        return instance;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public void registerCallback(Context context, ActivityMainBinding activityMainBinding) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(),
                networkCallback(context, activityMainBinding, new Handler(Looper.getMainLooper())));
    }

    @SuppressLint("SetTextI18n")
    private ConnectivityManager.NetworkCallback networkCallback(Context context, ActivityMainBinding activityMainBinding, Handler handler) {
        return new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                handler.post(() -> {
                    activityMainBinding.textViewInternet.setTextColor(ContextCompat.getColor(context, R.color.black_60));
                    activityMainBinding.linearLayoutInternet.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                    activityMainBinding.linearLayoutInternet.setVisibility(View.VISIBLE);
                    activityMainBinding.textViewInternet.setText("Network available");
                    activityMainBinding.imageViewInternet.setImageResource(R.drawable.ic_cloud_available);

                    handler.postDelayed(() -> {
                        activityMainBinding.linearLayoutInternet.setVisibility(View.GONE);
                    }, 4000);

                    Log.d("Tag", "Network available");
                });
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                handler.post(() -> {
                    activityMainBinding.textViewInternet.setTextColor(ContextCompat.getColor(context, R.color.black_60));
                    activityMainBinding.linearLayoutInternet.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
                    activityMainBinding.linearLayoutInternet.setVisibility(View.VISIBLE);
                    activityMainBinding.textViewInternet.setText("Network unavailable");
                    activityMainBinding.imageViewInternet.setImageResource(R.drawable.ic_cloud_lost);

                    handler.postDelayed(() -> {
                        activityMainBinding.linearLayoutInternet.setVisibility(View.GONE);
                    }, 4000);

                    Log.d("Tag", "Network unavailable");
                });
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                handler.post(() -> {
                    activityMainBinding.textViewInternet.setTextColor(ContextCompat.getColor(context, R.color.black_60));
                    activityMainBinding.linearLayoutInternet.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                    activityMainBinding.linearLayoutInternet.setVisibility(View.VISIBLE);
                    activityMainBinding.textViewInternet.setText("Network lost");
                    activityMainBinding.imageViewInternet.setImageResource(R.drawable.ic_cloud_lost);

                    handler.postDelayed(() -> {
                        activityMainBinding.linearLayoutInternet.setVisibility(View.GONE);
                    }, 4000);

                    Log.d("Tag", "Network lost");
                });
            }
        };
    }

}
