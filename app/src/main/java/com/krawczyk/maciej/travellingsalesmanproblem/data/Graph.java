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

    public int getIndexOf(double latitude, double longitude) {
        for (GraphPoint point : points) {
            if (latitude == point.getLat() && longitude == point.getLon()) {
                return points.indexOf(point);
            }
        }
        return -1;
    }

    public void addEdgesForPoint(MapPoint mapPointStart, ArrayList<AdjacencyPoint> adjacencies) {
        RealmList<AdjacencyPoint> adjacencyPoints = new RealmList<>();

        adjacencyPoints.addAll(adjacencies);

        points.add(new GraphPoint(mapPointStart.getName(), mapPointStart.getLatLng().latitude, mapPointStart.getLatLng().longitude, adjacencyPoints));
    }

    public int getEdgesCount() {
        return ((points.size() * (points.size() - 1)) / 2);
    }

    public int getVerticesCount() {
        return points.size();
    }
}
