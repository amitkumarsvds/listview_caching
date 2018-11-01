package com.android.lazyloading.recyclerview.retrofit;


import com.android.lazyloading.recyclerview.models.Proficiency;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface for REST API
 */
public interface Api {

    @GET("/s/2iodh4vg0eortkl/facts.json")
    Call<Proficiency> getFactsFromApi();
}
