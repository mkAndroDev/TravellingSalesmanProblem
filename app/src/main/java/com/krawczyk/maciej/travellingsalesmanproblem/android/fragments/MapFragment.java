package com.krawczyk.maciej.travellingsalesmanproblem.android.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog alertDialog;
    private ArrayList<LatLng> points = new ArrayList<>();

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getView();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        alertDialog = new AlertDialog.Builder(getContext()).create();

        return view;
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(latLng -> {
            points.add(latLng);
            mMap.addMarker(new MarkerOptions().position(latLng).title(points.size() + " " + getString(R.string.marker_counter_label)));
        });

        LatLng lodz = new LatLng(51.747858, 19.405815);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lodz));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setCurrentLocation();
        }
    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showAlertDialog(getString(R.string.ask_permission_localization_label),
                    (dialog, which) -> {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    },
                    (dialog, which) -> alertDialog.dismiss(),
                    true);
        } else {
            setCurrentLocation();
        }

    }

    private void setCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                LatLng currentLatLon = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(currentLatLon).title(getString(R.string.marker_here_you_are_label)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLon, 12.0f));
                for (LatLng latLng : points) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title(points.indexOf(latLng)
                            + " " + getString(R.string.marker_counter_label)));
                }
            }
        } else {
            requestPermissions();
        }
    }

    public void showAlertDialog(String message, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel, boolean cancelable) {
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dialog_ok), ok);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancel == null ? null : getString(R.string.dialog_cancel), cancel);
        alertDialog.setCancelable(cancelable);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }
}
