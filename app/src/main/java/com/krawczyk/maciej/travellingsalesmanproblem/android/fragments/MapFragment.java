package com.krawczyk.maciej.travellingsalesmanproblem.android.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Toast;

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
import com.krawczyk.maciej.travellingsalesmanproblem.android.activities.MainActivity;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Graph;
import com.krawczyk.maciej.travellingsalesmanproblem.data.MapPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MainActivity.MainActivityListener {

    private final static LatLng lodz = new LatLng(51.747858, 19.405815);

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog alertDialog;
    private ArrayList<MapPoint> points = new ArrayList<>();
    private LatLng currentPlace;
    Realm realm;

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

        ((MainActivity) getActivity()).setupMainActivityListener(this);

        setupViews();

        setupGooleApiClient();
        realm = Realm.getDefaultInstance();

        return view;
    }

    private void setupViews() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        alertDialog = new AlertDialog.Builder(getContext()).create();
    }

    private void setupGooleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
            int id = points.size();
            String name = getAddress(latLng);
            points.add(new MapPoint(id, name, latLng));
            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        });

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

    private void requestPermissions() {
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

    private void showAlertDialog(String message, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel, boolean cancelable) {
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dialog_ok), ok);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancel == null ? null : getString(R.string.dialog_cancel), cancel);
        alertDialog.setCancelable(cancelable);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                for (MapPoint mapPoint : points) {
                    mMap.addMarker(new MarkerOptions().position(mapPoint.getLatLng()).title(mapPoint.getName()));
                }
                currentPlace = currentLatLon;
            }
        } else {
            requestPermissions();
        }
    }

    private String getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address address = addresses.get(0);
            return address.getAddressLine(0) + ", " + address.getLocality();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onMenuItemClicked(int menuItemId) {
        switch (menuItemId) {
            case R.id.nav_clear_map:
                mMap.clear();
                points.clear();
                mMap.addMarker(new MarkerOptions().title(getString(R.string.marker_here_you_are_label)).position(currentPlace));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 12.0f));
                break;
            case R.id.nav_calculate_route:
                Graph graph = getPreparedGraph();
                Toast.makeText(getContext(), "Graph edges: " + graph.getEdgesCount() + ", vertices: " + graph.getVerticesCount(), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    private Graph getPreparedGraph() {
        realm.beginTransaction();
        Graph graph = realm.createObject(Graph.class);
        for (MapPoint pointStart : points) {
            ArrayList<MapPoint> endPoints = new ArrayList<>();
            ArrayList<Double> weights = new ArrayList<>();
            for (MapPoint pointEnd : points) {
                if (!pointStart.getLatLng().equals(pointEnd.getLatLng())) {
                    endPoints.add(pointEnd);
                    weights.add(10.34);
                }
            }
            graph.addEdgesForPoint(pointStart, endPoints, weights);
        }
        realm.commitTransaction();
        return graph;
    }
}
