package com.android.lazyloading.recyclerview.services.networkmanager;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.lazyloading.recyclerview.retrofit.ExerciseService;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Application class which initialze the retrofit instance and contains internet checks
 */
public class LazyLoadApplication extends Application {

    private ExerciseService mApiService;
    private ConnectionListener mConnectionListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * set the connection listener
     *
     * @param listener listener
     */
    public void setInternetConnectionListener(ConnectionListener listener) {
        mConnectionListener = listener;
    }

    /**
     * remove the connection listner
     */
    public void removeInternetConnectionListener() {
        mConnectionListener = null;
    }

    /**
     * returns retrofit instance
     *
     * @return mApiService
     */
    public ExerciseService getmApiService() {
        if (mApiService == null) {
            mApiService = provideRetrofit(Utils.BASE_URL).create(ExerciseService.class);
        }
        return mApiService;
    }

    /**
     * method to check internet availability
     *
     * @return
     */
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * returns retrofit instance
     *
     * @return mApiService
     */
    private Retrofit provideRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    /**
     * method to store data in cache
     *
     * @return cache
     */
    private Cache getCache() {
        File cacheDir = new File(getCacheDir(), "cache");
        Cache cache = new Cache(cacheDir, Utils.DISK_CACHE_SIZE);
        return cache;
    }

    /**
     * okhttp setup with timeout and intercepter for cache
     *
     * @return okhttpClientBuilder
     */
    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.cache(getCache());
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);

        okhttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return LazyLoadApplication.this.isInternetAvailable();
            }

            @Override
            public void onInternetUnavailable() {
                if (mConnectionListener != null) {
                    mConnectionListener.onInternetUnavailable();
                }
            }

            @Override
            public void onCacheUnavailable() {
                if (mConnectionListener != null) {
                    mConnectionListener.onCacheUnavailable();
                }
            }
        });

        return okhttpClientBuilder.build();
    }
}

