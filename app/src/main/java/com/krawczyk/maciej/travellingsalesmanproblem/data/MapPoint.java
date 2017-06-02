package com.krawczyk.maciej.travellingsalesmanproblem.data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by maciek on 30.05.17.
 */
public class MapPoint {

    private int index;
    private String name;
    private LatLng latLng;

    public MapPoint(int index, String name, LatLng latLng) {
        this.index = index;
        this.name = name;
        this.latLng = latLng;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public String toString() {
        return latLng.toString();
    }
}
