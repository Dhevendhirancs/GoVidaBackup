/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Every presenter in the app must either implement this interface or extend BasePresenter indicating the MvpView type that wants to be attached with.
 * Date : 15 April 19
 */

package com.govida.ui_section.base_class_section;

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

    // void handleApiError(ANError error);

    void setUserAsLoggedOut();
}
