package com.krawczyk.maciej.travellingsalesmanproblem.android.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.krawczyk.maciej.travellingsalesmanproblem.data.AdjacencyPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Graph;
import com.krawczyk.maciej.travellingsalesmanproblem.data.GraphPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Route;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by maciek on 09.06.17.
 */
public class Utils {

    public static String getAddress(Geocoder geocoder, LatLng latLng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address address = addresses.get(0);
            return address.getAddressLine(0) + ", " + address.getLocality();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Route getBestRoute(Graph graph) {

        Route route = new Route();

        route.addPoint(graph.getPoints().first());
        GraphPoint graphPoint = graph.getPoints().first();

        for (int i = 0; i < graph.getPoints().size(); i++) {

            int indexOfBestAdjacency = -1;
            int bestWeight = 999999999;

            for (AdjacencyPoint adjacency : graphPoint.getAdjacencyPoints()) {
                if (!route.containsPoint(adjacency.getPointEndLat(), adjacency.getPointEndLon()) &&
                        bestWeight >= adjacency.getWeight()) {
                    indexOfBestAdjacency = graphPoint.getAdjacencyPoints().indexOf(adjacency);
                    bestWeight = adjacency.getWeight();
                }
            }

            if (indexOfBestAdjacency >= 0) {
                AdjacencyPoint bestAdjacency = graphPoint.getAdjacencyPoints().get(indexOfBestAdjacency);

                int indexOfBestGraphPoint = graph.getIndexOf(bestAdjacency.getPointEndLat(), bestAdjacency.getPointEndLon());

                route.addPoint(graph.getPoints().get(indexOfBestGraphPoint));
                route.addDistance(bestAdjacency.getWeight());

                graphPoint = graph.getPoints().get(indexOfBestGraphPoint);
            }
        }

        graphPoint = route.getPoints().get(route.getPoints().size() - 1);
        for (AdjacencyPoint adjacency : graphPoint.getAdjacencyPoints()) {
            if (adjacency.getPointEndLat() == graph.getPoints().first().getLat() &&
                    adjacency.getPointEndLon() == graph.getPoints().first().getLon()) {
                route.addPoint(graph.getPoints().first());
                route.addDistance(adjacency.getWeight());
            }
        }
        return route;
    }

}
