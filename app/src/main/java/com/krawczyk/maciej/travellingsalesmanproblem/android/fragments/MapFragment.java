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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.krawczyk.maciej.travellingsalesmanproblem.R;
import com.krawczyk.maciej.travellingsalesmanproblem.android.Utils.Utils;
import com.krawczyk.maciej.travellingsalesmanproblem.data.AdjacencyPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Graph;
import com.krawczyk.maciej.travellingsalesmanproblem.data.GraphPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.MapPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Route;
import com.krawczyk.maciej.travellingsalesmanproblem.domain.MapFragmentView;
import com.krawczyk.maciej.travellingsalesmanproblem.domain.presenters.MapFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MapFragmentView, CalculatedRouteFragment.OnCalculatedRouteFragmentListener {

    private final static LatLng lodz = new LatLng(51.747858, 19.405815);

    private MapFragmentPresenter mapFragmentPresenter = new MapFragmentPresenter();

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog alertDialog;
    private ArrayList<MapPoint> points = new ArrayList<>();
    private LatLng currentPlace;
    private Route route;

    @BindView(R.id.map_view)
    MapView mapView;

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

        ButterKnife.bind(this, view);

        setupListeners();

        setupViews(savedInstanceState);

        setupGoogleApiClient();

        return view;
    }

    private void setupViews(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        alertDialog = new AlertDialog.Builder(getContext()).create();
    }

    private void setupListeners() {
        getMainActivity().setupMainActivityListener(this);
        mapFragmentPresenter.setMapFragmentView(this);
    }

    private void setupGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();


    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
            String name = Utils.getAddress(getContext(), latLng);
            points.add(new MapPoint(name, latLng));
            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        });

        if (route != null) {
            showRoute(route);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(route.getPoints().get(0).getLatLng(), 12.0f));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lodz, 12.0f));
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (route == null) {
            setCurrentLocation();
        }
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

    @Override
    public void onMenuItemClicked(int menuItemId) {
        switch (menuItemId) {
            case R.id.nav_clear_map:
                mMap.clear();
                points.clear();
                route = null;
                mMap.addMarker(new MarkerOptions().title(getString(R.string.marker_here_you_are_label)).position(currentPlace));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 12.0f));
                break;
            case R.id.nav_calculate_route:
                route = null;
                if (points.size() >= 2) {
                    mapFragmentPresenter.getPreparedDistances(points, getString(R.string.google_matrix_key));
                } else {
                    Toast.makeText(getContext(), getText(R.string.add_more_points), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDistancesTaken(List<AdjacencyPoint> allWeights) {
        getRealm().beginTransaction();
        Graph graph = getRealm().createObject(Graph.class);
        ArrayList<AdjacencyPoint> pointAdjacencies = new ArrayList<>();
        for (MapPoint point : points) {
            pointAdjacencies.clear();
            for (AdjacencyPoint adjacencyPoint : allWeights) {
                if (adjacencyPoint.getPointStartLat() == point.getLatLng().latitude && adjacencyPoint.getPointStartLon() == point.getLatLng().longitude) {
                    pointAdjacencies.add(adjacencyPoint);
                }
            }
            graph.addEdgesForPoint(point, pointAdjacencies);
        }

        getRealm().commitTransaction();

        mapFragmentPresenter.calculateRoute(graph);


        Toast.makeText(getContext(), getText(R.string.distances_success), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRouteCalculated(Route route) {
        showRoute(route);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), getText(R.string.distances_error) + " " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRouteOnMap(Route route) {
        this.route = route;
    }

    @Override
    public void onShowOnMapClicked(Route route) {
        Toast.makeText(getContext(), " OKOŃ ", Toast.LENGTH_LONG).show();
    }

    private void showRoute(Route route) {
        mMap.clear();
        for (GraphPoint graphPoint : route.getPoints()) {
            LatLng latLng = new LatLng(graphPoint.getLat(), graphPoint.getLon());
            mMap.addMarker(new MarkerOptions().title(graphPoint.getName()).position(latLng).title(route.getPoints().indexOf(graphPoint) + " localization"));

            if (route.getPoints().indexOf(graphPoint) < route.getPoints().size() - 1) {
                GraphPoint nextGraphPoint = route.getPoints().get(route.getPoints().indexOf(graphPoint) + 1);
                LatLng latLngOfNext = new LatLng(nextGraphPoint.getLat(), nextGraphPoint.getLon());

                mMap.addPolyline(new PolylineOptions()
                        .add(latLng, latLngOfNext)
                        .geodesic(true));
            } else {
                GraphPoint firstGraphPoint = route.getPoints().get(0);
                LatLng latLngOfNext = new LatLng(firstGraphPoint.getLat(), firstGraphPoint.getLon());

                mMap.addPolyline(new PolylineOptions()
                        .add(latLng, latLngOfNext)
                        .geodesic(true));
            }
        }
    }
}
