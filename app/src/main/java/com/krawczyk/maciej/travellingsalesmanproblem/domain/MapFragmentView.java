package com.krawczyk.maciej.travellingsalesmanproblem.domain;

import com.krawczyk.maciej.travellingsalesmanproblem.data.PointAdjacency;

import java.util.List;

/**
 * Created by maciek on 08.06.17.
 */
public interface MapFragmentView {

    void onDistancesTaken(List<PointAdjacency> allWeights);

    void onError(String message);

}
