package com.example.quiettimeapp.GeoFencing;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.example.quiettimeapp.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeoFencing extends AppCompatActivity implements OnMapReadyCallback {

    private static final String GEOFENCE_ACTION = "com.example.geo_fencing.ACTION_GEOFENCE";

    private GoogleMap googleMap;
    private MapView mapView;
    private Marker selectedMarker;
    private Button btnSaveLocation;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    public static List<Geofence> geofenceList = new ArrayList<>();
    LocationManager locationManager;
    LatLng lastKnownLatLng;
    Location location;
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
        setContentView(R.layout.geofencing);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(getBaseContext());
        mapView.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceList = new ArrayList<>();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null)
            lastKnownLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        else
            Log.d("ekkada","Location is null in geofence");

        ImageButton btnPinCurrent = (ImageButton) findViewById(R.id.pinCurrent);
        btnPinCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowLastknownLocation();
            }
        });

        btnSaveLocation = findViewById(R.id.btn_add);
        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMarker == null){
                     ShowAlertDialog("No location selected","Choose a location to create geofence around it!");
                }else {
                    addGFenceToSelectedLocation();
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        ShowLastknownLocation();

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                if (selectedMarker != null) {
                    selectedMarker.remove();
                }
                selectedMarker = googleMap.addMarker(new MarkerOptions().position(latLng));
                createGeofence(latLng);
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void ShowLastknownLocation(){
        if(location!=null)
            lastKnownLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        else {
            Log.d("ekkada", "Location is null in geofence");
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lastKnownLatLng = new LatLng(location.getLatitude(),location.getLongitude());

        googleMap.addMarker(new MarkerOptions().position(lastKnownLatLng).title("Device's Last Known Location"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng,100));
    }

    private void createGeofence(LatLng latLng) {
        /*
        if (geofencePendingIntent != null) {
            geofencingClient.removeGeofences(geofencePendingIntent);
        }
         */
        Slider slider = findViewById(R.id.slider);
        float GEOFENCE_RADIUS = slider.getValue();

        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(GEOFENCE_RADIUS)
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150));
        googleMap.addCircle(circleOptions);

        Geofence geofence = new Geofence.Builder()
                .setRequestId("${MAPS_API_KEY}" + geofenceList.size())
                .setCircularRegion(latLng.latitude, latLng.longitude, GEOFENCE_RADIUS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        geofenceList.add(geofence);

    }

    @SuppressLint("MissingPermission")
    public void addGFenceToSelectedLocation() {

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GeoFencing.this, "Geofence created successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GeoFencing.this, "Failed to create geofence", Toast.LENGTH_SHORT).show();
                        String errorMessage = getErrorMessage(e);
                        Log.d("ekkada", errorMessage);
                    }
                });
    }

    private GeofencingRequest getGeofencingRequest() {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(geofenceList)
                .build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(this, 9999, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        return geofencePendingIntent;
    }

    public String getErrorMessage(Exception e){
        if(e instanceof ApiException){
            ApiException apiException = (ApiException) e;
            switch(apiException.getStatusCode()){
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
                case GeofenceStatusCodes.GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION:
                    return "GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION";
                case GeofenceStatusCodes.GEOFENCE_REQUEST_TOO_FREQUENT:
                    return "GEOFENCE_REQUEST_TOO_FREQUENT";
            }
        }
        return e.getLocalizedMessage();
    }

    public void ShowAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNeutralButton("OK", (DialogInterface.OnClickListener)(dialog, which) ->{
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
/*
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
 */
}
