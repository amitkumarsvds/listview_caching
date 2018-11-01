package com.android.lazyloading.recyclerview.lazyload;

import android.support.v7.widget.LinearLayoutManager;

import com.android.lazyloading.recyclerview.R;
import com.android.lazyloading.recyclerview.adapters.LazyLoadAdpter;
import com.android.lazyloading.recyclerview.adapters.VerticalLineDecorator;
import com.android.lazyloading.recyclerview.models.Proficiency;
import com.android.lazyloading.recyclerview.services.networkmanager.LazyLoadApplication;

/**
 * presenter class for lazyload
 */
public class LazyLoadPresenter {
    private LazyLoadAdpter mAdapter;
    private LazyLoadActivity mContext;
    private LazyLoadApplication app;

    LazyLoadPresenter(LazyLoadActivity context, LazyLoadApplication application) {
        this.mContext = context;
        this.app = application;
    }

    /**
     * load the recycle view item here/upate the UI after response
     *
     * @param proficiency proficiency
     */
    public void updateUI(final Proficiency proficiency) {
        if (proficiency == null) {
            return;
        }
        if (proficiency.getTitle() == null) {
            mContext.getSupportActionBar().
                    setTitle(mContext.getString(R.string.NA));
        }
        mContext.getSupportActionBar().setTitle(proficiency.getTitle());
        mAdapter = new LazyLoadAdpter(mContext, proficiency.getRows());
        mContext.mRecyclerView.setHasFixedSize(true);
        mContext.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mContext.mRecyclerView.addItemDecoration(new VerticalLineDecorator(2));
        mContext.mRecyclerView.setAdapter(mAdapter);

    }
}