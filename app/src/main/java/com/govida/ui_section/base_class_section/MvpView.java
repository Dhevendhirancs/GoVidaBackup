/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 *         pattern must implement. Generally this interface will be extended by a more specific interface
 *         that then usually will be implemented by an Activity or Fragment.
 * Date : 15 April 19
 */

package com.govida.ui_section.base_class_section;


import androidx.annotation.StringRes;

public interface MvpView {

    void showLoading();

    void hideLoading();

    void openActivityOnTokenExpire();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();

}
