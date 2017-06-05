package com.krawczyk.maciej.travellingsalesmanproblem.data;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by maciek on 30.05.17.
 */
@RealmClass
public class Graph extends RealmObject {

    private RealmList<GraphPoint> points = new RealmList<>();

    public Graph() {
        // Required empty public constructor
    }

    public RealmList<GraphPoint> getPoints() {
        return points;
    }

    public void addEdgesForPoint(MapPoint mapPointStart, ArrayList<MapPoint> endMapPoints, ArrayList<Integer> weights) {
        RealmList<PointAdjacency> adjacencyPoints = new RealmList<>();

        for (int i = 0; i < endMapPoints.size(); i++) {
            PointAdjacency pointAdjacency = new PointAdjacency(mapPointStart.getLatLng().latitude, mapPointStart.getLatLng().longitude,
                    endMapPoints.get(i).getLatLng().latitude, endMapPoints.get(i).getLatLng().longitude, weights.get(i));
            adjacencyPoints.add(pointAdjacency);
        }

        points.add(new GraphPoint(mapPointStart.getLatLng().latitude, mapPointStart.getLatLng().longitude, adjacencyPoints));
    }

    public int getEdgesCount() {
        return ((points.size() * (points.size() - 1)) / 2);
    }

    public int getVerticesCount() {
        return points.size();
    }
}
