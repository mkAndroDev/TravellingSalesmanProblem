package com.krawczyk.maciej.travellingsalesmanproblem.data;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by maciek on 02.06.17.
 */
@RealmClass
public class PointAdjacency extends RealmObject {

    private double pointStartLat;
    private double pointStartLon;
    private double pointEndLat;
    private double pointEndLon;
    private int weight;

    PointAdjacency(double pointStartLat, double pointStartLon, double pointEndLat, double pointEndLon, Integer weight) {
        this.pointStartLat = pointStartLat;
        this.pointStartLon = pointStartLon;
        this.pointEndLat = pointEndLat;
        this.pointEndLon = pointEndLon;
        this.weight = weight;
    }

    public PointAdjacency() {
        // Required empty public constructor
    }

    public PointAdjacency(LatLng pointStart, LatLng pointEnd, int weight) {
        this.pointStartLat = pointStart.latitude;
        this.pointStartLon = pointStart.longitude;
        this.pointEndLat = pointEnd.latitude;
        this.pointEndLon = pointEnd.longitude;
        this.weight = weight;
    }

    public double getPointStartLat() {
        return pointStartLat;
    }

    public void setPointStartLat(double pointStartLat) {
        this.pointStartLat = pointStartLat;
    }

    public double getPointStartLon() {
        return pointStartLon;
    }

    public void setPointStartLon(double pointStartLon) {
        this.pointStartLon = pointStartLon;
    }

    public double getPointEndLat() {
        return pointEndLat;
    }

    public void setPointEndLat(double pointEndLat) {
        this.pointEndLat = pointEndLat;
    }

    public double getPointEndLon() {
        return pointEndLon;
    }

    public void setPointEndLon(double pointEndLon) {
        this.pointEndLon = pointEndLon;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return pointStartLat + ", " + pointStartLon + " : " + pointEndLat + ", " + pointEndLon + " -> " + weight;
    }
}
