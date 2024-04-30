package com.example.quiettimeapp;

import static android.app.PendingIntent.FLAG_MUTABLE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Context.MODE_PRIVATE;

import static com.example.quiettimeapp.GeneralLocations.GLocationPendingIntentHolder.pendingIntent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.quiettimeapp.GeneralLocations.GLocationPendingIntentHolder;
import com.example.quiettimeapp.GeneralLocations.GeneralLocations;
import com.example.quiettimeapp.GeneralLocations.LocationUpdatesReceiver;
import com.example.quiettimeapp.GeoFencing.GeoFencing;
import com.example.quiettimeapp.TimeTable.AlarmReceiver;
import com.example.quiettimeapp.TimeTable.TimetableSetUpActivity;
import com.example.quiettimeapp.Wifi.WifiBroadcastReceiver;
import com.example.quiettimeapp.Wifi.WifiSetUpActivity;

import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreference;
    SharedPreferences.Editor editSharedPreferences;
    Drawable drawableON, drawableOFF;

    WifiBroadcastReceiver wifiBroadcastReceiver = new WifiBroadcastReceiver();
    IntentFilter intentFilter = new IntentFilter();
    String [] onOffbuttonStrings = new String[] {"LocationOnOff", "ScheduleOnOff", "GeoFencingOnOff", "WifiOnOff"};

    PermissionsHelper permissionsHelper;
    LocationManager locationManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getSupportActionBar().hide();
        }
        catch(Exception e){
            Log.d("exceptions", Arrays.toString(e.getStackTrace()));
        }
        setContentView(R.layout.mainmenu);

        context = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        permissionsHelper = new PermissionsHelper();
        if(!permissionsHelper.getDNDAccessPermission()){
            permissionsHelper.ShowAlertDialog("DND Access Denied","Grant the required permissions to avail the features of the app");
            return;
        }
        if(permissionsHelper.getFineLocationPermission()){
            permissionsHelper.getBackgroundLocation();
        }

        ImageButton [] imageButtons = new ImageButton[]
                {
                        (ImageButton)findViewById(R.id.btnOnOffschedule),
                        (ImageButton)findViewById(R.id.btnOnOffgeneralLocations),
                        (ImageButton)findViewById(R.id.btnOnOffgeoLocation),
                        (ImageButton)findViewById( R.id.btnOnOffwifi)
                };

        sharedPreference = getSharedPreferences("OnOffDataBase", MODE_PRIVATE);

        drawableON = AppCompatResources.getDrawable(this,R.drawable.onoff_btn_bg_green);
        drawableOFF = AppCompatResources.getDrawable(this,R.drawable.onoff_btn_bg_red);
        for(int i=0; i<4; i++)
            imageButtons[i].setBackground(sharedPreference.getBoolean(onOffbuttonStrings[i],false) ? drawableON : drawableOFF);

        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        if(sharedPreference.getBoolean("WifiOnOff",false))
            registerReceiver(wifiBroadcastReceiver,intentFilter);
    }

    private boolean ChangeBgOfOnOff(View V, int i)
    {
        ImageButton imageButton = (ImageButton) V;
        editSharedPreferences = sharedPreference.edit();
        boolean on_or_Off = sharedPreference.getBoolean(onOffbuttonStrings[i],false);
        imageButton.setBackground( on_or_Off ? drawableOFF : drawableON);
        editSharedPreferences.putBoolean(onOffbuttonStrings[i],!on_or_Off);
        editSharedPreferences.apply();
        return !on_or_Off;
    }

    public void ScheduleOnOffListener(View V)
    {
        if(!permissionsHelper.getDNDAccessPermission()) return;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent8550525 = new Intent(this, AlarmReceiver.class);
        Intent intent855 = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent855 = PendingIntent.getBroadcast(this,855,intent855,FLAG_UPDATE_CURRENT | FLAG_MUTABLE);

        Calendar calendar;
        boolean on_or_off = ChangeBgOfOnOff(V,0);
        if(on_or_off)
        {
            Calendar current=Calendar.getInstance();
            calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 55);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            int currentHour=current.get(Calendar.HOUR_OF_DAY);
            int currentMin=current.get(Calendar.MINUTE);
            int totalCurrentMin=currentHour*60+currentMin;
            Log.d("ekkada", String.valueOf(totalCurrentMin));
            if( totalCurrentMin > 535 && totalCurrentMin <  1045)
            {
                PendingIntent pendingIntent8550525 = PendingIntent.getBroadcast(this,8550525,intent8550525,PendingIntent.FLAG_CANCEL_CURRENT | FLAG_MUTABLE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,0,pendingIntent8550525);
            }
            // generate a 8:55 pending intent should be done in any condition (below, above or in-between)
            if(current.after(calendar)) {
                calendar.add(Calendar.DATE, 1);
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent855);
        }
        else
        {
            AudioManager audioManagerobj = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManagerobj.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            PendingIntent pendingIntent8550525 = PendingIntent.getBroadcast(this,8550525,intent8550525,PendingIntent.FLAG_CANCEL_CURRENT | FLAG_MUTABLE);
            alarmManager.cancel(pendingIntent855);
            alarmManager.cancel(pendingIntent8550525);
            pendingIntent8550525.cancel();
            pendingIntent855.cancel();
        }
    }

    public void LocationOnOffListener(View V) {
        if(!permissionsHelper.checkAllPermissions(onOffbuttonStrings[1])) return;
        boolean on_or_off = ChangeBgOfOnOff(V,1);

        if(on_or_off){
            Intent intent = new Intent(this, LocationUpdatesReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 69999, intent, PendingIntent.FLAG_MUTABLE);
            GLocationPendingIntentHolder.pendingIntent = pendingIntent;
        }else{
            if (GLocationPendingIntentHolder.pendingIntent != null) {
                GLocationPendingIntentHolder.pendingIntent.cancel();
            }
        }
    }

    public void GFencingOnOffListener(View V) {
        if(!permissionsHelper.checkAllPermissions(onOffbuttonStrings[2])) return;
        ChangeBgOfOnOff(V,2);
    }

    public void WifiOnOffListener(View V)
    {
        if(!permissionsHelper.checkAllPermissions(onOffbuttonStrings[3])) return;

        boolean on_or_off = ChangeBgOfOnOff(V,3);
        if(on_or_off){ registerReceiver(wifiBroadcastReceiver,intentFilter); }
        else{ unregisterReceiver(wifiBroadcastReceiver); }
    }

    public void ScheduleSetUpListener(View V)
    {
        if(!permissionsHelper.getDNDAccessPermission()) return;

        Intent intent = new Intent(this, TimetableSetUpActivity.class);
        startActivity(intent);
    }

    public void LocationSetUpListener(View V)
    {
        if(!permissionsHelper.checkAllPermissions(onOffbuttonStrings[1])) return;

        Intent intent = new Intent(this, GeneralLocations.class);
        startActivity(intent);
    }

    public void GFencingSetUpListener(View V)
    {
        if(!permissionsHelper.checkAllPermissions(onOffbuttonStrings[2]))return;

        Intent intent = new Intent(this, GeoFencing.class);
        startActivity(intent);
    }

    public void WifiSetUpListener(View V)
    {
        if(!permissionsHelper.checkAllPermissions(onOffbuttonStrings[3]))return;

        Intent intent = new Intent(this, WifiSetUpActivity.class);
        startActivity(intent);
    }

    public class PermissionsHelper {
        PermissionsHelper(){
            sharedPreference = getSharedPreferences("OnOffDataBase", MODE_PRIVATE);
        }

        public boolean getFineLocationPermission(){
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }
            return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        public boolean getBackgroundLocation(){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
            }
            return true;
        }
        public boolean getDNDAccessPermission(){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (!notificationManager.isNotificationPolicyAccessGranted())
            {
                Intent i = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(i);
            }
            return notificationManager.isNotificationPolicyAccessGranted();
        }

        public boolean checkAllPermissions(String whichButton){
            if(!sharedPreference.getBoolean(whichButton,false)){
                if(!(getDNDAccessPermission() && getFineLocationPermission() && getBackgroundLocation())) return false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if(!locationManager.isLocationEnabled()){
                        ShowAlertDialog("Location is disabled","Enable the locations to avail the feature");
                        return false;
                    }
                }
            }
            return true;
        }

        private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) {
                ShowAlertDialog("Permission Denied!", "The features of the app don't work without the required permissions. So, restart the app to give the denied permission");
            }
        });

        public void ShowAlertDialog( String title, String message){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message);
            builder.setTitle(title);
            builder.setNeutralButton("OK", (DialogInterface.OnClickListener)(dialog, which) ->{
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}



