package com.example.quiettimeapp.GeneralLocations;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.quiettimeapp.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Collections;
import java.util.List;


public class LocationUpdatesReceiver extends BroadcastReceiver {
    double latitude, longitude;
    PlacesClient placesClient;
    LocationManager locationManager;
    SharedPreferences sharedPreference;
    Context context;
    int noOfSwitches;
    boolean []lTypesArray;

    Place.Type []requiredTypes = new Place.Type[]{
            Place.Type.HOSPITAL,
            Place.Type.LIBRARY,
            Place.Type.SCHOOL,
            Place.Type.UNIVERSITY,
            Place.Type.PLACE_OF_WORSHIP,
            Place.Type.LOCAL_GOVERNMENT_OFFICE,
            Place.Type.MOVIE_THEATER,
            Place.Type.RESTAURANT,
            Place.Type.BANK
    };

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager == null) return;
        if(!locationManager.isLocationEnabled()){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Location is disabled");
            builder.setTitle("Enable the locations to avail the feature");
            builder.setNeutralButton("OK", (DialogInterface.OnClickListener)(dialog, which) ->{
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }

        Location location = intent.getExtras().getParcelable(LocationManager.KEY_LOCATION_CHANGED);
        if (location == null) return;;

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("ekkada", "latitude : " + String.valueOf(latitude )+"longitude" + String.valueOf(longitude));

        Places.initialize(context, BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(context);

        List<Place.Field> placeFields = Collections.singletonList(Place.Field.TYPES);

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
        @SuppressWarnings("MissingPermission")
        final Task<FindCurrentPlaceResponse> placeResult = placesClient.findCurrentPlace(request);
        placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    noOfSwitches = 9;
                    lTypesArray = new boolean[noOfSwitches];
                    sharedPreference = context.getSharedPreferences("GeneralLocationsSwitches", MODE_PRIVATE);

                    for(int i=0; i < noOfSwitches; i++)
                        lTypesArray[i] = sharedPreference.getBoolean(String.valueOf(i+1),false);

                    FindCurrentPlaceResponse likelyPlaces = task.getResult();
                    for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                        Log.d("ekkada",placeLikelihood.getLikelihood()+"-"+placeLikelihood.getPlace().getTypes());

                        if( placeLikelihood.getLikelihood() < 0.75 ) return;
                        List<Place.Type> givenTypes = placeLikelihood.getPlace().getTypes();
                        if(givenTypes == null) return;
                        for(int i=0; i<noOfSwitches; i++){
                            if(lTypesArray[i]){
                                if(givenTypes.get(0) == requiredTypes[i] || givenTypes.get(1) == requiredTypes[i] ){
                                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                    return;
                                }
                            }
                        }
                    }
                }
                else {
                    Log.d("ekkada", "Exception: %s", task.getException());
                }
            }
        });
    }
}
