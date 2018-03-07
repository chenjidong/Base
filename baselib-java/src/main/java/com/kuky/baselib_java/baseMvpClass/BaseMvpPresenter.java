package com.kuky.baselib_java.baseMvpClass;

/**
 * @author Kuky
 */
public abstract class BaseMvpPresenter<V extends BaseMvpViewImpl> {
    protected V mView;

    protected void attach(V view) {
        this.mView = view;
    }

    protected void detach() {
        this.mView = null;
    }

    protected void onResume() {

    }

    protected void onStop() {

    }

    protected void onPause() {

    }
}
