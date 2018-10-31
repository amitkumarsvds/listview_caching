package com.android.lazyloading.recyclerview.lazyload;

import android.app.Application;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.android.lazyloading.recyclerview.R;
import com.android.lazyloading.recyclerview.adapters.LazyLoadAdpter;
import com.android.lazyloading.recyclerview.adapters.VerticalLineDecorator;
import com.android.lazyloading.recyclerview.alert.LazyLoadAlertDialog;
import com.android.lazyloading.recyclerview.models.Row;
import com.android.lazyloading.recyclerview.services.networkmanager.Utils;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotSame;

/**
 * presenter class for lazyload
 */
public class LazyLoadPresenter implements LazyLoadViewServiceCommunicator.OnServiceCallFinishedListener {

    private LazyLoadView mExeciseView;
    private LazyLoadViewServiceCommunicator mLazyLoadViewServiceCommunicator;
    private List mItemLoaded = new ArrayList();
    private boolean mFlag;
    private LazyLoadAdpter mAdapter;
    private LazyLoadActivity mContext;
    private Application app;


    LazyLoadPresenter(LazyLoadView execiseView, LazyLoadViewServiceCommunicator lazyLoadViewServiceCommunicator,Application application) {
        this.mExeciseView = execiseView;
        this.mLazyLoadViewServiceCommunicator = lazyLoadViewServiceCommunicator;
        mContext = ((LazyLoadActivity) mExeciseView);
        this.app = application;
        mLazyLoadViewServiceCommunicator.serviceFacts(application, this);//API call to get list of facts data
    }

    /**
     * releasing view (exercise activity context)
     */
    public void onDestroy() {
        mExeciseView = null;
    }


    /**
     * load the recycle view item here/upate the UI after response
     *
     * @param respRows respRows
     */
    private void updateUI(final List<Row> respRows, String title) {
        if (title == null) {
            mContext.getSupportActionBar().
                    setTitle(mContext.getResources().getString(R.string.NA));
        } else {
            mContext.getSupportActionBar().setTitle(title);
        }

        if (!mFlag) {
            for (int i = 0; i <= Utils.INCREMENTCOUNT; i++) {
                mItemLoaded.add(respRows.get(i));
            }
            mAdapter = new LazyLoadAdpter((LazyLoadActivity) mExeciseView, mItemLoaded);
            mAdapter.setLoadMoreListener(new LazyLoadAdpter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    mExeciseView.showProgressBar();

                    mAdapter.setMoreDataAvailable(false);

                    mContext.mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int index = mItemLoaded.size();
                            int newSize = 0;
                            if (index + Utils.INCREMENTCOUNT < respRows.size())
                                newSize = index + Utils.INCREMENTCOUNT;
                            else
                                newSize = respRows.size();
                            for (int i = index; i < newSize; i++) {
                                mItemLoaded.add(respRows.get(i));
                            }
                            mAdapter.notifyDataChanged();
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mExeciseView.hideProgressBar();

                                    if (mItemLoaded.size() < respRows.size())
                                        mAdapter.setMoreDataAvailable(true);
                                }
                            });
                        }
                    }, Utils.DELAYTIME);
                }
            });
            mContext.mRecyclerView.setHasFixedSize(true);
            mContext.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mContext.mRecyclerView.addItemDecoration(new VerticalLineDecorator(2));
            mContext.mRecyclerView.setAdapter(mAdapter);

        }
    }

    @Override
    public void onSuccess(final List<Row> respRows, String title) {

        assertNotSame(null, respRows);

        mFlag = false;

        if (mExeciseView != null) {
            mExeciseView.hideSwipeRefresh();

            mExeciseView.dismissProgressDialog();
        }

        updateUI(respRows, title);
    }

    @Override
    public void onFailure(String failureData) {

        // asserts failure response string is empty or not
        assertNotSame(failureData, "");

        if (mExeciseView != null) {
            mExeciseView.hideSwipeRefresh();

            mExeciseView.dismissProgressDialog();

            LazyLoadAlertDialog.alertDilaog((LazyLoadActivity) mExeciseView, failureData);
        }


    }

    public void refresh() {

        mFlag = true;//Will not allow to set any item in list if mFlag is true
        mItemLoaded.clear();//clearing old data from list

        //asserting to verify size is 0 or not post clearing it - refer previous line
        //     assertEquals(0, mItemLoaded.size());

        //referesh the list item
        if (mAdapter != null) {
            mAdapter.notifyDataChanged();
        }

        Log.d("refresh", "refresh");
        mLazyLoadViewServiceCommunicator.serviceFacts(app, this);
    }
}