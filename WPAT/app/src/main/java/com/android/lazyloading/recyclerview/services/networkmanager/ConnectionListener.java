package com.android.lazyloading.recyclerview.services.networkmanager;

/**
 * Interface for cache and internet unavilable
 */
public interface ConnectionListener {

    void onInternetUnavailable();

    void onCacheUnavailable();
}
