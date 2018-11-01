package com.android.lazyloading.recyclerview.lazyload;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.android.lazyloading.recyclerview.R;
import com.android.lazyloading.recyclerview.alert.LazyLoadAlertDialog;
import com.android.lazyloading.recyclerview.models.Proficiency;
import com.android.lazyloading.recyclerview.retrofit.Api;
import com.android.lazyloading.recyclerview.services.networkmanager.ConnectionListener;
import com.android.lazyloading.recyclerview.services.networkmanager.LazyLoadApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * class which will show list of item
 */
public class LazyLoadActivity extends AppCompatActivity implements LazyLoadView, ConnectionListener {

    protected RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;
    private LazyLoadPresenter mPresenter;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        if (savedInstanceState == null) {
            showProgressDialog();
        }
        ((LazyLoadApplication) getApplication()).setInternetConnectionListener(this);
        mPresenter = new LazyLoadPresenter(LazyLoadActivity.this, (LazyLoadApplication) getApplication());
        mRecyclerView = findViewById(R.id.recycler_view);
        swipeLayout = findViewById(R.id.swipe_refresh);
        setUiElements();


        //ViewModel responsibility is to manage the data for the UI.

        final LazyLoadViewModel model = ViewModelProviders.of(this).get(LazyLoadViewModel.class);
        model.getProficienyData().observe(this, new Observer<Proficiency>() {
            @Override
            public void onChanged(@Nullable Proficiency proficiency) {
                if (proficiency != null) {
                    // update ui.
                    mPresenter.updateUI(proficiency);
                }
                dismissProgressDialog();
            }
        });
        // Adding  Swipe view Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Api api = ((LazyLoadApplication) getApplication()).getApiService();
                Call<Proficiency> call = api.getFactsFromApi();
                call.enqueue(new Callback<Proficiency>() {
                    @Override
                    public void onResponse(Call<Proficiency> call, Response<Proficiency> response) {
                        mPresenter.updateUI(response.body());
                    }

                    @Override
                    public void onFailure(Call<Proficiency> call, Throwable t) {

                    }
                });
                hideSwipeRefresh();
            }
        });

    }

    /**
     * Initialize the UI elements here
     */
    @Override
    public void setUiElements() {
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LazyLoadAlertDialog.alertDilaog(LazyLoadActivity.this,
                        getString(R.string.networkmessage));
            }
        });


    }

}
