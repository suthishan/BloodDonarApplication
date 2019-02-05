package com.suthishan.blooddonar.views;

public interface LoginViews {
    void showProgress();
    void hideProgress();
    void loginSuccess(String response);
    void loginError(String string);
}
