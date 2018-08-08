package com.veritagis.blitz3shopping.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.veritagis.blitz3shopping.R;
import com.veritagis.blitz3shopping.Utils.ConnectivityReceiver;
import com.veritagis.blitz3shopping.Utils.MyApplication;


public class MainActivity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Button btn_go, btn_menu;
    private RelativeLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        btn_go = findViewById(R.id.btn_go);
        btn_menu = findViewById(R.id.btn_menu);

        activity_main = findViewById(R.id.activity_main);

        MyApplication.getInstance().setConnectivityListener(this);
        checkConnection();

        btn_go.setOnClickListener(new View.OnClickListener() { // go to landscape mode activity
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LandScapeFeedActivity.class);
                intent.putExtra("from_go", "from_go");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetPopup actionSheetPopup = new ActionSheetPopup(MainActivity.this);
                actionSheetPopup.show();
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(MainActivity.this, LandScapeFeedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        //   String message ="";
        int color;
        if (isConnected) {
            //     message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            String message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(activity_main, "" +
                            message, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
        // for rotation
        new CountDownTimer(5000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                int orientation = getResources().getConfiguration().orientation;
                if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
                    Intent intent = new Intent(MainActivity.this, LandScapeFeedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }.start();
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
