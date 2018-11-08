package com.android.lazyloading.recyclerview.lazyload;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.lazyloading.recyclerview.R;
import com.android.lazyloading.recyclerview.models.Proficiency;
import com.android.lazyloading.recyclerview.retrofit.Api;
import com.android.lazyloading.recyclerview.services.networkmanager.LazyLoadApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * class for viewmodel
 */
public class LazyLoadViewModel extends AndroidViewModel {
    private MutableLiveData<Proficiency> mProficienyList;
    private LazyLoadApplication mLazyLoadApplication;

    public LazyLoadViewModel(@NonNull Application application) {
        super(application);
        mLazyLoadApplication = (LazyLoadApplication) application;
    }

    /**
     * call this method to get the data
     *
     * @return mProficienyList
     */
    public LiveData<Proficiency> getProficienyData() {
        //if the list is null
        if (mProficienyList == null) {
            mProficienyList = new MutableLiveData<Proficiency>();
            //we will load it asynchronously from server in this method
            loadDatas();
        }
        //finally we will return the list
        return mProficienyList;
    }

    private void loadDatas() {
        Api api = mLazyLoadApplication.getApiService();
        Call<Proficiency> call = api.getFactsFromApi();
        call.enqueue(new Callback<Proficiency>() {
            @Override
            public void onResponse(Call<Proficiency> call, Response<Proficiency> response) {
                Log.d("JSONNNN","Service");
                mProficienyList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Proficiency> call, Throwable t) {
                Toast.makeText(mLazyLoadApplication.getApplicationContext(), mLazyLoadApplication.getApplicationContext().getString(R.string.failure_invalid_response), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * get the latest data when user click on refresh button/swipe view
     */
    public void getLatestFeed()
    {
        loadDatas();
    }
}
