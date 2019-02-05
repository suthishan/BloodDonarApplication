package com.suthishan.blooddonar.views;

public interface RegisterViews {
    void showProgress();
    void hideProgress();
    void registerSuccess(String response);
    void regsiterError(String string);
}
