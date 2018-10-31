package com.android.lazyloading.recyclerview.services.networkmanager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * abstract class for cache/intercepter
 */
public abstract class NetworkConnectionInterceptor implements Interceptor {

    public abstract boolean isInternetAvailable();

    public abstract void onInternetUnavailable();

    public abstract void onCacheUnavailable();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isInternetAvailable()) {
            onInternetUnavailable();
            request = request.newBuilder().header("Cache-Control",
                    "public, only-if-cached, max-stale=" + 10).build();//max-scale value can be customize depends on requirment
            Response response = chain.proceed(request);
            if (response.cacheResponse() == null) {
                onCacheUnavailable();
            }
            return response;
        }
        return chain.proceed(request);
    }
}
