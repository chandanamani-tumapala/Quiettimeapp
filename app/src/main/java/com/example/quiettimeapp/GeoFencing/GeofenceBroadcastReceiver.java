package com.example.quiettimeapp.GeoFencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e("GeofenceBroadcastReceiver", "GeofencingEvent error: " + geofencingEvent.getErrorCode());
            return;
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        AudioManager audioManagerobj = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            audioManagerobj.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            audioManagerobj.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
}