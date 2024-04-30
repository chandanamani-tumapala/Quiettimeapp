package com.example.quiettimeapp.GeneralLocations;

import static com.example.quiettimeapp.GeneralLocations.GLocationPendingIntentHolder.pendingIntent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.quiettimeapp.R;
import com.example.quiettimeapp.TimeTable.DataBaseHelper;

import java.util.Arrays;

public class GeneralLocations extends AppCompatActivity {
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editSharedPreferences;
    SwitchCompat[] switches;
    private int noOfSwitches = 8;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getSupportActionBar().hide();
        } catch (Exception e) {
            Log.d("exceptions", Arrays.toString(e.getStackTrace()));
        }
        setContentView(R.layout.activity_general_locations);
        switches = new SwitchCompat[noOfSwitches];

        sharedPreference = getSharedPreferences("GeneralLocationsSwitches", MODE_PRIVATE);
        int ids[] = new int[]{R.id.switchHospitals, R.id.switchLibraries, R.id.switchSchoolsEducational, R.id.switchWorshipPlaces, R.id.switchGovernmentOffices, R.id.switchTheaters, R.id.switchRestaurants, R.id.switchBank };
        for(int i=0; i < noOfSwitches; i++) {
            switches[i] = (SwitchCompat) findViewById(ids[i]);
        }
        showSwitchesStateFromStorage();
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for(int i=0; i < noOfSwitches; i++) {
                    if( buttonView.getId() == ids[i]) {
                        updateSwitchStateInStorage(String.valueOf(i + 1), isChecked);
                        break;
                    }
                }
            }
        };

        for(int i=0 ; i<noOfSwitches ; i++)
            switches[i].setOnCheckedChangeListener(listener);

        Intent intent = new Intent(this, LocationUpdatesReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 69999, intent, PendingIntent.FLAG_MUTABLE);
        GLocationPendingIntentHolder.pendingIntent = pendingIntent;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, pendingIntent);
    }
    private void showSwitchesStateFromStorage(){
        for(int i=0; i < noOfSwitches; i++){
            switches[i].setChecked(sharedPreference.getBoolean(String.valueOf(i+1),false));
        }
    }
    private void updateSwitchStateInStorage(String whichSwitch, boolean whatToUpdate){
        if(whichSwitch==null) return;
        editSharedPreferences = sharedPreference.edit();
        editSharedPreferences.putBoolean(whichSwitch, whatToUpdate);
        editSharedPreferences.apply();
    }
}