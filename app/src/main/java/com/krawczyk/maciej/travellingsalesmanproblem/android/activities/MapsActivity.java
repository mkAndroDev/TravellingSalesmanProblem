package com.krawczyk.maciej.travellingsalesmanproblem.android.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.krawczyk.maciej.travellingsalesmanproblem.R;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog alertDialog;
    private ArrayList<LatLng> points = new ArrayList<>();

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        alertDialog = new AlertDialog.Builder(this).create();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(latLng -> {
            points.add(latLng);
            mMap.addMarker(new MarkerOptions().position(latLng).title(points.size() + " Marker"));
        });

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlertDialog("Aby móc pobrać aktualną lokalizację potrzebujemy zgody na dostęp do lokalizacji.",
                        (dialog, which) -> {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                        },
                        (dialog, which) -> alertDialog.dismiss(),
                        true);
                requestPermissions();
            } else {
                setCurrentLocation();
            }
        }
    }

    private void setCurrentLocation() {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng currentLatLon = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(currentLatLon).title("Here you are"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLon, 12.0f));
            for (LatLng latLng : points) {
                mMap.addMarker(new MarkerOptions().position(latLng).title(points.indexOf(latLng) + " marker"));
            }
        }
    }

    public void showAlertDialog(String message, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel, boolean cancelable) {

        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok", ok);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancel == null ? null : "anuluj", cancel);
        alertDialog.setCancelable(cancelable);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }
}
