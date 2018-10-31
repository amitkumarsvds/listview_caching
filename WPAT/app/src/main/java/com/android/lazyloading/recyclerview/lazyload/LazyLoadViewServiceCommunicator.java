package com.android.lazyloading.recyclerview.lazyload;


import android.app.Application;
import android.util.Log;

import com.android.lazyloading.recyclerview.models.Proficiency;
import com.android.lazyloading.recyclerview.models.Row;
import com.android.lazyloading.recyclerview.services.networkmanager.LazyLoadApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class for service call and response
 */
public class LazyLoadViewServiceCommunicator {

    private String mGetTitle;

    /**
     * api call to fetch facts from server through retrofit API communication
     */
    void serviceFacts(Application app, final OnServiceCallFinishedListener listener) {

        Log.d("refresh", "service");

        //  private void getFactsFromApi() {
        ((LazyLoadApplication) app).getmApiService().getFactsFromApi()
                .enqueue(new Callback<Proficiency>() {
                    @Override
                    public void onResponse(Call<Proficiency> call, Response<Proficiency> response) {
                        if (response.isSuccessful()) {
                            // return to UI thread
                            // display AccountInfo on UI

                            Log.d("refresh", "response" + response.toString());

                            if (response.body() == null) {
                                listener.onFailure("No data available");
                            } else {
                                mGetTitle = response.body().getTitle();
                                Log.d("refresh", "data " + response.body().getRows().toString());

                                listener.onSuccess(response.body().getRows(), mGetTitle);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Proficiency> call, Throwable t) {
                        listener.onFailure("Invalid Response.. Please try after sometime!");
                    }
                });
        //  }

    }


    interface OnServiceCallFinishedListener {

        void onSuccess(final List<Row> respRows, String title);

        void onFailure(String failureData);
    }

}