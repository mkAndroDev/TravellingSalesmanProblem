package com.krawczyk.maciej.travellingsalesmanproblem.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by maciek on 02.06.17.
 */
@RealmClass
public class GraphPoint extends RealmObject {

    private double lat;
    private double lng;
    private RealmList<PointAdjacency> adjacencyPoints;

    GraphPoint(double lat, double lng, RealmList<PointAdjacency> adjacencyPoints) {
        this.lat = lat;
        this.lng = lng;
        this.adjacencyPoints = adjacencyPoints;
    }

    public GraphPoint() {
        // Required empty public constructor
    }

    public double getLat() {

        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public RealmList<PointAdjacency> getAdjacencyPoints() {
        return adjacencyPoints;
    }

    public void setAdjacencyPoints(RealmList<PointAdjacency> adjacencyPoints) {
        this.adjacencyPoints = adjacencyPoints;
    }
}
