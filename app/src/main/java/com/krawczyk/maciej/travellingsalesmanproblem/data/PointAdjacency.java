package com.krawczyk.maciej.travellingsalesmanproblem.data;

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
    private Double weight;

    PointAdjacency(double pointStartLat, double pointStartLon, double pointEndLat, double pointEndLon, Double weight) {
        this.pointStartLat = pointStartLat;
        this.pointStartLon = pointStartLon;
        this.pointEndLat = pointEndLat;
        this.pointEndLon = pointEndLon;
        this.weight = weight;
    }

    public PointAdjacency() {
        // Required empty public constructor
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
