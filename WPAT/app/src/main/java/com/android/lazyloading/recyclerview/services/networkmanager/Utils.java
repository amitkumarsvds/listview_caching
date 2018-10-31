package com.android.lazyloading.recyclerview.services.networkmanager;

/**
 * Utils class for constant variables
 */
public class Utils {

     private  Utils(){}
     /*
     Constant variable
     */
     public static final String BASE_URL = "https://dl.dropboxusercontent.com";//Base Url
     public static final int DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10 MB cache size
     public static final int DELAYTIME = 2000;//Load more.. delaytime
     public static final int INCREMENTCOUNT = 7;//Load only 7 data at time in list
}
