package com.android.lazyloading.recyclerview;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import com.android.lazyloading.recyclerview.lazyload.LazyLoadActivity;
import com.android.lazyloading.recyclerview.models.Proficiency;
import com.android.lazyloading.recyclerview.retrofit.Api;
import com.android.lazyloading.recyclerview.services.networkmanager.LazyLoadApplication;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LazyLoadActivityTest extends ActivityInstrumentationTestCase2<LazyLoadActivity> {
    private LazyLoadActivity mTestActivity;
    private LazyLoadApplication mLazyLoadApplication;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    public LazyLoadActivityTest() {
        super(LazyLoadActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTestActivity = getActivity();
        mLazyLoadApplication = (LazyLoadApplication) mTestActivity.getApplicationContext();
        mSwipeRefreshLayout = mTestActivity.findViewById(R.id.swipe_refresh);
        mRecyclerView = mTestActivity.findViewById(R.id.recycler_view);
    }

    @Test
    public void test_Activity_Is_Null_checking() {
        assertNotNull("mTestActivity is null", mTestActivity);
    }

    @Test
    public void test_SwipeRefreshLayout_Is_Null_checking() {
        assertNotNull("mSwipeRefreshLayout is null", mSwipeRefreshLayout);
    }

    @Test
    public void test_SwipeRefresBehaviour() {
        assertFalse(mSwipeRefreshLayout.isRefreshing());
    }

    @Test
    public void test_TwoTimes_SwipeRefresBehaviour() {
        assertFalse(mSwipeRefreshLayout.isRefreshing());
        assertFalse(mSwipeRefreshLayout.isRefreshing());
    }

    @Test
    public void test_RecyclerView_Is_Null_checking() {
        assertNotNull("mRecyclerView is null", mRecyclerView);
    }

    @Test
    public void testResponseData() {
        Api api = mLazyLoadApplication.getApiService();
        Call<Proficiency> call = api.getFactsFromApi();
        call.enqueue(new Callback<Proficiency>() {
            @Override
            public void onResponse(Call<Proficiency> call, Response<Proficiency> response) {
                assertEquals(response.isSuccessful(), true);
                // fails if 2nd post in response does not have title "Flag"
                // this test case is against the static data being received from API
                assertEquals(response.body().getRows().get(1).getTitle(), "Flag");
            }

            @Override
            public void onFailure(Call<Proficiency> call, Throwable t) {
             // skip for now
            }
        });


    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (mTestActivity != null) {
            mTestActivity = null;
        }
        if (mLazyLoadApplication != null) {
            mLazyLoadApplication = null;
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout = null;
        }
        if (mRecyclerView != null) {
            mRecyclerView = null;
        }
    }
}