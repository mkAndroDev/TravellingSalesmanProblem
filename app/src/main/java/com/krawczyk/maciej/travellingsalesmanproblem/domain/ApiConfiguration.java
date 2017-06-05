package com.krawczyk.maciej.travellingsalesmanproblem.domain;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maciejkrawczyk on 03.06.2017.
 */
public class ApiConfiguration {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/";

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
