package com.android.lazyloading.recyclerview.lazyload;

/**
 * Interface Exerciseview
 */
public interface LazyLoadView {

    void setUiElements();

    void showProgressDialog();

    void dismissProgressDialog();

    void hideSwipeRefresh();
}
