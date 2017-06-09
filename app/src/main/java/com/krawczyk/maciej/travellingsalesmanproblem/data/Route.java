package com.krawczyk.maciej.travellingsalesmanproblem.data;

import java.util.ArrayList;

/**
 * Created by maciek on 09.06.17.
 */

public class Route {

    private ArrayList<Integer> distances = new ArrayList<>();
    private ArrayList<GraphPoint> points = new ArrayList<>();

    public void addDistance(int distance) {
        distances.add(distance);
    }

    public ArrayList<Integer> getDistances() {
        return distances;
    }

    public int getDistanceForRoute() {
        int distanceForRoute = 0;
        for (int distance : distances) {
            distanceForRoute += distance;
        }
        return distanceForRoute;
    }

    public void addPoint(GraphPoint point) {
        points.add(point);
    }

    public ArrayList<GraphPoint> getPoints() {
        return points;
    }

    public boolean containsPoint(double pointLat, double pointLon) {
        for (GraphPoint point : points) {
            if (point.getLat() == pointLat && point.getLon() == pointLon) {
                return true;
            }
        }
        return false;
    }
}
