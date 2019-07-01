/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : BasePresenter for providing common presenter usage
 * Date : 15 April 19
 */

package com.govida.ui_section.base_class_section;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V>{
    private static final String TAG = "BasePresenter";
    private V mMvpView;
    /*
    private final DataManager mDataManager;

    @Inject
    protected BasePresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }
    protected DataManager getDataManager() {
        return mDataManager;
    }

    */
    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
    }

    private boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }



    @Override
    public void setUserAsLoggedOut() {
        //getDataManager().setAccessToken(null);
    }


    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
