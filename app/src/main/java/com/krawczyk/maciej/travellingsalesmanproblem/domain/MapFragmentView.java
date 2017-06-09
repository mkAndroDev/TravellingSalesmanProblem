package com.krawczyk.maciej.travellingsalesmanproblem.domain;

import com.krawczyk.maciej.travellingsalesmanproblem.data.AdjacencyPoint;
import com.krawczyk.maciej.travellingsalesmanproblem.data.Route;

import java.util.List;

/**
 * Created by maciek on 08.06.17.
 */
public interface MapFragmentView {

    void onDistancesTaken(List<AdjacencyPoint> allWeights);

    void onRouteCalculated(Route route);

    void onError(String message);

}
