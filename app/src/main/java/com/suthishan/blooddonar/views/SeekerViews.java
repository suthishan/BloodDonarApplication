package com.suthishan.blooddonar.views;

public interface SeekerViews {
    void showProgress();
    void hideProgress();
    void seekerListSuccess(String response);
    void seekerListError(String string);
}
