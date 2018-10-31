package com.android.lazyloading.recyclerview;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ProficiencyInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        /* checking package is same as mentioned in manifest (com.android.lazyloading.recyclerview)*/
        assertEquals("com.android.lazyloading.recyclerview", appContext.getPackageName());

        /* checking app name is same as mentioned in manifest (Lazy View)*/
        assertEquals("Lazy View", appContext.getResources().getString(R.string.app_name));

        /* checking network connectivity
        passes/succeeds     if network is available
        fails/cries         if there is no network */
        // assertEquals(true, NetworkConnectivityManager.isNetworkAvailable(appContext));

    }

}
