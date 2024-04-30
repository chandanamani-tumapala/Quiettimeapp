package com.example.quiettimeapp.Wifi;

import static android.net.wifi.WifiManager.UNKNOWN_SSID;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiettimeapp.R;

import java.util.Arrays;
import java.util.Objects;

public class WifiSetUpActivity extends AppCompatActivity {
    String connectedSSID;
    String alertTitle = "No WIFI network Detected !";
    String alertMessageEnableAndConnect = "Enable and Connect to a WIFI to avail the service";
    String alertMessageConnect = "Connect to a WIFI to add";
    WifiManager wifiManager;
    DataBaseHelperForWifi dataBaseHelperForWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getSupportActionBar().hide();
        } catch (Exception e) {
            Log.d("exceptions", Arrays.toString(e.getStackTrace()));
        }
        setContentView(R.layout.activity_wifi);

        if (!isWifiEnabled()) {
            ShowAlertDialog(alertTitle, alertMessageEnableAndConnect);
        } else {
            getConnectedSSID();
            TextView connectedTo = (TextView) findViewById(R.id.tv_ConnectedTo);
            connectedTo.setText(connectedSSID);
        }
    }

    private void getConnectedSSID() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        connectedSSID = wifiInfo.getSSID();
    }

    private boolean isWifiEnabled() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public void addSSID(View view) {
        if (isWifiEnabled()) {
            dataBaseHelperForWifi = new DataBaseHelperForWifi(getBaseContext());

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                if (!Objects.equals(connectedSSID, UNKNOWN_SSID)) {
                    if ((!dataBaseHelperForWifi.SearchInSSIDs(connectedSSID)))
                        dataBaseHelperForWifi.addSSID(connectedSSID);
                    else
                        ShowAlertDialog("Already Added", "The current wifi network is already added!");
                } else {
                    ShowAlertDialog("UnknownSSID", "Unable to detect the connected WIFI name!");
                }
                SharedPreferences sharedPreference = getSharedPreferences("OnOffDataBase", MODE_PRIVATE);
                if (sharedPreference.getBoolean("WifiOnOff", false))
                    PutToDND();
            } else {
                ShowAlertDialog(alertTitle, alertMessageConnect);
            }
        } else {
            ShowAlertDialog(alertTitle, alertMessageEnableAndConnect);
        }
    }

    private void PutToDND() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    public void ShowAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNeutralButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBaseHelperForWifi.CloseDataBase();
    }
}