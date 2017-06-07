package com.krawczyk.maciej.travellingsalesmanproblem.domain;

import com.krawczyk.maciej.travellingsalesmanproblem.domain.models.DistanceMatrix;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by maciejkrawczyk on 03.06.2017.
 */
public interface Endpoints {

//    https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY&key=YOUR_API_KEY


//    @GET("json")
    @GET("?units=imperial&origins={origins}&destinations={destinations}&key=AIzaSyBaMpWKIIunKsaBVvdx8x5pWsvFTusa780")
    Call<DistanceMatrix> getDistance(@Query("origins") String origin, @Query("destinations") String destination);
//    Call<DistanceMatrix> getDistance(@Query("origins") String origin, @Query("destinations") String destination, @Query("key") String key);

}
