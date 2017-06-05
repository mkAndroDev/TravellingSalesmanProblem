package com.krawczyk.maciej.travellingsalesmanproblem.domain;

import com.krawczyk.maciej.travellingsalesmanproblem.domain.models.DistanceMatrix;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by maciejkrawczyk on 03.06.2017.
 */
public interface Endpoints {

    @GET("json")
    Call<DistanceMatrix> getDistance(@Query("origins") String origin, @Query("destinations") String destination, @Query("key") String key);

}
