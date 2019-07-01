/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 *         pattern must implement. Generally this interface will be extended by a more specific interface
 *         that then usually will be implemented by an Activity or Fragment.
 * Date : 15 April 19
 */

package com.govida.ui_section.base_class_section;


public interface SubMvpView extends MvpView {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void attachParentMvpView(MvpView mvpView);
}
