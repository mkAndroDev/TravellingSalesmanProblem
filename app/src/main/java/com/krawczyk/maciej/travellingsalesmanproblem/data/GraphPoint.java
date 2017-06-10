package com.krawczyk.maciej.travellingsalesmanproblem.data;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by maciek on 02.06.17.
 */
@RealmClass
public class GraphPoint extends RealmObject {

    private String name;
    private double lat;
    private double lon;
    private RealmList<AdjacencyPoint> adjacencyPoints;

    public GraphPoint(String name, double lat, double lng, RealmList<AdjacencyPoint> adjacencyPoints) {
        this.name = name;
        this.lat = lat;
        this.lon = lng;
        this.adjacencyPoints = adjacencyPoints;
    }

    public GraphPoint() {
        // Required empty public constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lon);
    }

    public double getLat() {

        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public RealmList<AdjacencyPoint> getAdjacencyPoints() {
        return adjacencyPoints;
    }

    public void setAdjacencyPoints(RealmList<AdjacencyPoint> adjacencyPoints) {
        this.adjacencyPoints = adjacencyPoints;
    }

    @Override
    public String toString() {
        return lat + ", " + lon;
    }
}
