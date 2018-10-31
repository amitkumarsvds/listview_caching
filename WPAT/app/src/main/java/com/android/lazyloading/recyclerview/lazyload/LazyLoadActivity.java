package com.android.lazyloading.recyclerview.lazyload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.lazyloading.recyclerview.R;
import com.android.lazyloading.recyclerview.alert.LazyLoadAlertDialog;
import com.android.lazyloading.recyclerview.services.networkmanager.ConnectionListener;
import com.android.lazyloading.recyclerview.services.networkmanager.LazyLoadApplication;


/**
 * class which will show list of item
 */
public class LazyLoadActivity extends AppCompatActivity implements LazyLoadView, ConnectionListener {

    protected RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private LazyLoadPresenter presenter;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mContext = this;

        setUiElements();

        ((LazyLoadApplication) getApplication()).setInternetConnectionListener(this);

        presenter = new LazyLoadPresenter(this, new LazyLoadViewServiceCommunicator(),(LazyLoadApplication) getApplication());

        showProgressDialog();

        // Adding  Swipe view Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //delegating API call to presenter to get the list item from the server
                presenter.refresh();
            }
        });

    }

    /**
     * Initialize the UI elements here
     */
    @Override
    public void setUiElements() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progress_bar);
        swipeLayout = findViewById(R.id.swipe_refresh);
        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
    }

    /**
     * show progress dialog
     */
    @Override
    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.progress_message));
        mProgressDialog.setTitle(getResources().getString(R.string.progress_message_title));
        mProgressDialog.show();
    }

    /**
     * dismiss progress dialog
     */
    @Override
    public void dismissProgressDialog() {

        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    /**
     * show progress bar
     */
    @Override
    public void showProgressBar() {

        if (mProgressBar != null)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide progress bar
     */
    @Override
    public void hideProgressBar() {

        if (mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
    }

    /**
     * hide swipe refresh view
     */
    @Override
    public void hideSwipeRefresh() {
        swipeLayout.setRefreshing(false);
    }

    /**
     * activity destroy view
     */
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((LazyLoadApplication) getApplication()).setInternetConnectionListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ((LazyLoadApplication) getApplication()).removeInternetConnectionListener();
    }


    @Override
    public void onInternetUnavailable() {
        hideSwipeRefresh();

        dismissProgressDialog();

    }

    @Override
    public void onCacheUnavailable() {

        // blank on first time offline else cached of previous-online-data
        hideSwipeRefresh();

        dismissProgressDialog();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LazyLoadAlertDialog.alertDilaog(LazyLoadActivity.this,
                        mContext.getResources().getString(R.string.networkmessage));
            }
        });
    }
}
