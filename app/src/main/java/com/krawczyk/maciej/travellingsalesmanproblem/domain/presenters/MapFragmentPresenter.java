package com.krawczyk.maciej.travellingsalesmanproblem.domain.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.krawczyk.maciej.travellingsalesmanproblem.android.Utils.Utils;
import com.krawczyk.maciej.travellingsalesmanproblem.data.AdjacencyPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Graph;
import com.krawczyk.maciej.travellingsalesmanproblem.data.MapPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Route;
import com.krawczyk.maciej.travellingsalesmanproblem.domain.ApiConfiguration;
import com.krawczyk.maciej.travellingsalesmanproblem.domain.Endpoints;
import com.krawczyk.maciej.travellingsalesmanproblem.domain.MapFragmentView;
import com.krawczyk.maciej.travellingsalesmanproblem.domain.models.DistanceMatrix;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by maciek on 08.06.17.
 */
public class MapFragmentPresenter {

    private MapFragmentView mapFragmentView;
    private Endpoints endpoints = ApiConfiguration.retrofit.create(Endpoints.class);

    public void setMapFragmentView(MapFragmentView mapFragmentView) {
        this.mapFragmentView = mapFragmentView;
    }

    public void getPreparedDistances(List<MapPoint> points, String apiKey) {
        List<AdjacencyPoint> allWeights = new ArrayList<>();
        for (MapPoint pointStart : points) {
            for (MapPoint pointEnd : points) {
                if (!pointStart.getLatLng().equals(pointEnd.getLatLng())) {
                    Call<DistanceMatrix> distanceMatrix = endpoints.getDistance(pointStart.toString(), pointEnd.toString(), apiKey);
                    distanceMatrix.enqueue(new Callback<DistanceMatrix>() {
                        @Override
                        public void onResponse(@NonNull Call<DistanceMatrix> call, @NonNull Response<DistanceMatrix> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                int distance = response.body().getRows().get(0).getElements().get(0).getDistance().getValue();
                                Log.d("GetDistance", "response == isSuccessful, point1: " + points.indexOf(pointStart) + " point2: " + points.indexOf(pointEnd) + " distance: " + distance);

                                allWeights.add(new AdjacencyPoint(pointStart.getLatLng(), pointEnd.getLatLng(), distance));

                                if (allWeights.size() == points.size() * (points.size() - 1)) {
                                    mapFragmentView.onDistancesTaken(allWeights);
                                    Log.d("GetDistance", "Last one!");
                                }

                            } else {
                                Log.d("GetDistance", "response != isSuccessful");
                                mapFragmentView.onError(response.message());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DistanceMatrix> call, @NonNull Throwable t) {
                            Log.d("GetDistance", "onFailure");
                            mapFragmentView.onError(t.getMessage());
                        }
                    });
                }
            }
        }
    }

    public void calculateRoute(Graph graph) {
        Route route = Utils.getBestRoute(graph);

        mapFragmentView.onRouteCalculated(route);
    }

}
